package com.br.courses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Record para requisição de criação/atualização de curso
 */
public record CourseRequest(
    @NotBlank(message = "Título é obrigatório")
    String title,

    @NotBlank(message = "Descrição é obrigatória")
    String description,

    @NotBlank(message = "Categoria é obrigatória")
    String category,

    @Positive(message = "Duração deve ser maior que zero")
    Integer duration
) {
}
