package com.example.backend.dto;

import java.util.Objects;

public record MetaStatsDto(
        Long metaId,
        String metaName,
        Long matchesPlayed,
        Long gamesPlayed,
        Long wins,
        Long losses,
        Long draws,
        Double gameWinRate
) {
    public MetaStatsDto {
        // normalize nulls to zeros where appropriate
        if (matchesPlayed == null) matchesPlayed = 0L;
        if (gamesPlayed == null) gamesPlayed = 0L;
        if (wins == null) wins = 0L;
        if (losses == null) losses = 0L;
        if (draws == null) draws = 0L;
        if (gameWinRate == null) gameWinRate = 0.0;
    }
}
