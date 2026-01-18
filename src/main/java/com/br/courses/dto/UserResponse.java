package com.br.courses.dto;

/**
 * Record para resposta de usuário (sem senha por segurança)
 */
public record UserResponse(
    Long id,
    String username,
    String email,
    Boolean enabled,
    String role
) {
}

