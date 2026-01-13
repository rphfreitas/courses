package com.br.courses.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ValidationErrorResponse", description = "Resposta com múltiplos erros de validação")
public class ValidationErrorResponse {
    @Schema(description = "Timestamp da ocorrência do erro", example = "2026-01-13T20:30:00Z")
    private String timestamp;

    @Schema(description = "Código HTTP do erro", example = "400")
    private int status;

    @Schema(description = "Tipo de erro", example = "Bad Request")
    private String error;

    @Schema(description = "Lista de erros de validação encontrados")
    private List<ValidationError> errors;
}