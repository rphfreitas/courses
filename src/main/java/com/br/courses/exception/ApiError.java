package com.br.courses.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiError", description = "Resposta de erro genérica da API")
public class ApiError {
    @Schema(description = "Timestamp da ocorrência do erro", example = "2026-01-13T20:30:00Z")
    private String timestamp;

    @Schema(description = "Código HTTP do erro", example = "404")
    private int status;

    @Schema(description = "Tipo de erro", example = "Not Found")
    private String error;

    @Schema(description = "Mensagem descritiva do erro", example = "Curso não encontrado com id: 999")
    private String message;
}