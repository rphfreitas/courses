package com.br.courses.dto;

/**
 * Record para resposta de login com tokens JWT
 * Immutable e automaticamente gera equals, hashCode, toString e getter
 */
public record LoginResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn,
    String username
) {
    /**
     * Construtor compacto com valor padrão para tokenType
     */
    public LoginResponse {
        if (tokenType == null || tokenType.isEmpty()) {
            tokenType = "Bearer";
        }
    }

    /**
     * Factory method para criar LoginResponse
     */
    public static LoginResponse of(String accessToken, String refreshToken, Long expiresIn, String username) {
        return new LoginResponse(accessToken, refreshToken, "Bearer", expiresIn, username);
    }
}

