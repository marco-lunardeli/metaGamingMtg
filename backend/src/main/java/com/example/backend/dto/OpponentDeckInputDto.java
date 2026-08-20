package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record OpponentDeckInputDto(
    @NotBlank String name,
    Long metaId
) {}
