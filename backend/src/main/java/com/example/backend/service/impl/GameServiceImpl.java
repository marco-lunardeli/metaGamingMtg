package com.example.backend.service.impl;

import com.example.backend.dto.GameInputDto;
import com.example.backend.entity.Game;
import com.example.backend.entity.Match;
import com.example.backend.entity.GameResult;
import com.example.backend.repository.GameRepository;
import com.example.backend.repository.MatchRepository;
import com.example.backend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final MatchRepository matchRepository;

    @Override
    @Transactional
    public Game create(GameInputDto input) {
        Match match = matchRepository.findById(input.matchId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        Game game = Game.builder()
                .match(match)
                .gameNumber(input.gameNumber() != null ? input.gameNumber() : 1)
                .result(GameResult.valueOf(input.result().toUpperCase()))
                .build();

        // optional: update totalGames in Match
        match.setTotalGames(match.getTotalGames() + 1);

        return gameRepository.save(game);
    }
}
