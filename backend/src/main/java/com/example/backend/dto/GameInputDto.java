package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;

public record GameInputDto(
    @NotNull Long matchId,
    Integer gameNumber,
    @NotNull String result
) {}
