package com.example.backend.dto;

public record PairWinDto(
    Long deckId,
    String deckName,
    Long opponentDeckId,
    String opponentDeckName,
    Long matchesPlayed,
    Long gamesPlayed,
    Long wins,
    Long losses,
    Long draws,
    Double winRate
) {}
