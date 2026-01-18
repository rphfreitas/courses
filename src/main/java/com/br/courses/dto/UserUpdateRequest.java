package com.br.courses.dto;

import jakarta.validation.constraints.Email;

/**
 * Record para atualização de usuário
 */
public record UserUpdateRequest(
    @Email(message = "Email deve ser válido")
    String email,

    String role
) {
}

