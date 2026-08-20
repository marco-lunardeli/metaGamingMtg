package com.example.backend.service.impl;

import com.example.backend.dto.MatchInputDto;
import com.example.backend.entity.Match;
import com.example.backend.entity.OpponentDeck;
import com.example.backend.entity.Deck;
import com.example.backend.repository.MatchRepository;
import com.example.backend.repository.OpponentDeckRepository;
import com.example.backend.repository.DeckRepository;
import com.example.backend.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final DeckRepository deckRepository;
    private final OpponentDeckRepository opponentDeckRepository;

    @Override
    @Transactional
    public Match create(MatchInputDto input) {
        Deck deck = deckRepository.findById(input.deckId())
                .orElseThrow(() -> new IllegalArgumentException("Deck not found"));
        OpponentDeck opponent = null;
        if (input.opponentDeckId() != null) {
            opponent = opponentDeckRepository.findById(input.opponentDeckId())
                    .orElseThrow(() -> new IllegalArgumentException("Opponent deck not found"));
        }

        Match match = Match.builder()
                .deck(deck)
                .opponentDeck(opponent)
                .matchDate(input.matchDate())
                .totalGames(input.totalGames() != null ? input.totalGames() : 0)
                .build();

        return matchRepository.save(match);
    }
}
