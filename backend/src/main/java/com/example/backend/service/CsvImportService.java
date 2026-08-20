package com.example.backend.service;

import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CsvImportService {

    private final DeckRepository deckRepo;
    private final OpponentDeckRepository opponentRepo;
    private final MatchRepository matchRepo;
    private final GameRepository gameRepo;
    private final MetaRepository metaRepo;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CsvImportService(DeckRepository deckRepo,
                            OpponentDeckRepository opponentRepo,
                            MatchRepository matchRepo,
                            GameRepository gameRepo,
                            MetaRepository metaRepo) {
        this.deckRepo = deckRepo;
        this.opponentRepo = opponentRepo;
        this.matchRepo = matchRepo;
        this.gameRepo = gameRepo;
        this.metaRepo = metaRepo;
    }

    @Transactional
    public ImportReport importCsv(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();
        int lines = 0;
        int matchesCreated = 0;
        int gamesCreated = 0;
        Set<String> metasCreated = new HashSet<>();

        try (InputStream is = file.getInputStream();
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             CSVReader reader = new CSVReader(isr)) {

            String[] headers = reader.readNext();
            if (headers == null) throw new IOException("CSV vazio");
            Map<String, Integer> idx = headerIndex(headers);

            String[] row;
            while ((row = reader.readNext()) != null) {
                lines++;
                String opponentName = get(row, idx, "Oponente");
                String opponentDeckName = get(row, idx, "DeckAdversario");
                String deckName = get(row, idx, "Deck");
                String evento = get(row, idx, "Evento");
                String loja = get(row, idx, "Loja");
                String vitoriasStr = get(row, idx, "Vitorias");
                String derrotasStr = get(row, idx, "Derrotas");
                String resultadoCsv = get(row, idx, "Resultado");
                String dataStr = get(row, idx, "Data");
                String notas = get(row, idx, "Notas");

                // parse date
                LocalDate matchDate;
                try {
                    if (dataStr == null) throw new IllegalArgumentException("Data vazia");
                    matchDate = LocalDate.parse(dataStr.trim(), dtf);
                } catch (Exception e) {
                    errors.add("Linha " + lines + ": data inválida -> " + dataStr);
                    continue;
                }

                int vitorias = parseIntSafe(vitoriasStr, 0);
                int derrotas = parseIntSafe(derrotasStr, 0);
                int totalGames = vitorias + derrotas;
                if (totalGames == 0) {
                    errors.add("Linha " + lines + ": total de jogos é 0 (vitorias+derrotas)");
                    continue;
                }

                // procurar decks — criar automaticamente se ausente
                Deck deck = deckRepo.findByNameIgnoreCase(deckName == null ? "" : deckName.trim())
                        .orElseGet(() -> {
                            Deck d = Deck.builder()
                                    .name(deckName == null ? "unknown" : deckName.trim())
                                    .version(1)
                                    .build();
                            deckRepo.save(d);
                            errors.add("Linha " + lines + ": Deck criado automaticamente -> " + d.getName());
                            return d;
                        });

                OpponentDeck opponentDeck = opponentRepo.findByNameIgnoreCase(opponentDeckName == null ? "" : opponentDeckName.trim())
                        .orElseGet(() -> {
                            OpponentDeck od = OpponentDeck.builder()
                                    .name(opponentDeckName == null ? "unknown" : opponentDeckName.trim())
                                    .build();
                            opponentRepo.save(od);
                            errors.add("Linha " + lines + ": OpponentDeck criado automaticamente -> " + od.getName());
                            return od;
                        });

                // meta: loja + evento
                String metaName = buildMetaName(loja, evento);
                Meta meta = metaRepo.findByNameIgnoreCase(metaName).orElseGet(() -> {
                    Meta m = Meta.builder().name(metaName).build();
                    metaRepo.save(m);
                    metasCreated.add(metaName);
                    return m;
                });

                // associar meta ao opponentDeck se estiver vazio
                if (opponentDeck.getMeta() == null) {
                    opponentDeck.setMeta(meta);
                    opponentRepo.save(opponentDeck);
                } else if (!meta.getName().equalsIgnoreCase(opponentDeck.getMeta().getName())) {
                    errors.add("Linha " + lines + ": OpponentDeck '" + opponentDeck.getName() + "' já está associado ao meta '" + opponentDeck.getMeta().getName() + "'");
                }

                // find or create match
                Optional<Match> matchOpt = matchRepo.findByMatchDateAndOpponentDeckNameIgnoreCaseAndDeckNameIgnoreCase(matchDate, opponentDeck.getName(), deck.getName());
                Match match = matchOpt.orElseGet(() -> {
                    Match m = Match.builder()
                            .matchDate(matchDate)
                            .deck(deck)
                            .opponentDeck(opponentDeck)
                            .totalGames(totalGames)
                            .build();
                    matchRepo.save(m);
                    return m;
                });
                if (matchOpt.isEmpty()) matchesCreated++;

                // create games
                int gameNumber = match.getGames() == null ? 1 : match.getGames().size() + 1;
                List<Game> toPersist = new ArrayList<>();
                // vitorias -> WIN
                for (int i = 0; i < vitorias; i++) {
                    Game g = Game.builder()
                            .match(match)
                            .gameNumber(gameNumber++)
                            .result(GameResult.WIN)
                            .notes(notas)
                            .build();
                    toPersist.add(g);
                }
                for (int i = 0; i < derrotas; i++) {
                    Game g = Game.builder()
                            .match(match)
                            .gameNumber(gameNumber)
                            .result(GameResult.LOSS)
                            .notes(notas)
                            .build();
                    gameNumber++;
                    toPersist.add(g);
                }

                // persist games
                gameRepo.saveAll(toPersist);
                gamesCreated += toPersist.size();
            }
        } catch (CsvValidationException e) {
            throw new IOException("Erro lendo CSV: " + e.getMessage(), e);
        }

        return new ImportReport(lines, matchesCreated, gamesCreated, metasCreated, errors);
    }

    private Map<String, Integer> headerIndex(String[] headers) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i].trim(), i);
        }
        return map;
    }

    private String get(String[] row, Map<String, Integer> idx, String header) {
        Integer i = idx.get(header);
        if (i == null || i >= row.length) return null;
        String s = row[i];
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty() || s.equalsIgnoreCase("N/A")) return null;
        return s;
    }

    private int parseIntSafe(String s, int def) {
        if (s == null) return def;
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }

    private String buildMetaName(String loja, String evento) {
        String L = loja == null ? "" : loja.trim();
        String E = evento == null ? "" : evento.trim();
        String combined = (L + " - " + E).trim();
        return combined.isEmpty() ? "default" : combined;
    }

    public static class ImportReport {
        public final int lines;
        public final int matchesCreated;
        public final int gamesCreated;
        public final Set<String> metasCreated;
        public final List<String> errors;

        public ImportReport(int lines, int matchesCreated, int gamesCreated, Set<String> metasCreated, List<String> errors) {
            this.lines = lines;
            this.matchesCreated = matchesCreated;
            this.gamesCreated = gamesCreated;
            this.metasCreated = metasCreated;
            this.errors = errors;
        }
    }
}
