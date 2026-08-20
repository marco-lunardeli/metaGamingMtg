package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MatchInputDto(
    Long opponentDeckId,
    @NotNull Long deckId,
    @NotNull LocalDate matchDate,
    Integer totalGames
) {}
