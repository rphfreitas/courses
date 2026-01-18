package com.br.courses.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Record para requisição de criação de usuário
 */
public record UserCreateRequest(
    @NotBlank(message = "Username é obrigatório")
    String username,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    String password
) {
}

