package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeckInputDto(
    @NotBlank String name,
    Long formatId,
    @NotNull Long userId,
    Integer version
) {}
