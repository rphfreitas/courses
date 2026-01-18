package com.br.courses.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Record para requisição de refresh token
 * Immutable e automaticamente gera equals, hashCode, toString e getter
 */
public record RefreshTokenRequest(
    @NotBlank(message = "Refresh token é obrigatório")
    String refreshToken
) {
}

