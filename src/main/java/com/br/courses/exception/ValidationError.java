package com.br.courses.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ValidationError", description = "Erro de validação de um campo específico")
public class ValidationError {
    @Schema(description = "Nome do campo com erro", example = "name")
    private String field;

    @Schema(description = "Valor rejeitado do campo", example = "null")
    private Object rejectedValue;

    @Schema(description = "Mensagem de erro de validação", example = "O nome é obrigatório")
    private String message;
}