package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserInputDto (
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(max = 55, message = "O nome de usuário não pode ter mais de 55 caracteres")
    String username
)
{}
