package com.br.courses.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Record para requisição de login
 * Immutable e automaticamente gera equals, hashCode, toString e getter
 */
public record LoginRequest(
    @NotBlank(message = "Username é obrigatório")
    String username,

    @NotBlank(message = "Senha é obrigatória")
    String password
) {
}

