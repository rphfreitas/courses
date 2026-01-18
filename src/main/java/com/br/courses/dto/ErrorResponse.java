package com.br.courses.dto;

import java.time.LocalDateTime;

/**
 * Record para resposta de erro padronizada
 */
public record ErrorResponse(
    String error,
    String message,
    int status,
    String path,
    LocalDateTime timestamp
) {
    /**
     * Factory method para criar ErrorResponse
     */
    public static ErrorResponse of(String error, String message, int status, String path) {
        return new ErrorResponse(error, message, status, path, LocalDateTime.now());
    }
}

