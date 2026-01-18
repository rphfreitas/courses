package com.br.courses.dto;

/**
 * Record para resposta de curso
 */
public record CourseResponse(
    Long id,
    String title,
    String description,
    String category,
    Integer duration
) {
}

