package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record MetaInputDto(
    @NotBlank String name,
    Long userId
) {}
