package com.br.courses.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Schema(name = "Course", description = "Modelo de dados para um curso")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("_id")
    @Schema(description = "Identificador único do curso", example = "1")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
    @Column(length = 200, nullable = false)
    @Schema(description = "Nome do curso", example = "Spring Boot Fundamentals", maxLength = 200)
    private String name;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(max = 10, message = "A categoria deve ter no máximo 10 caracteres")
    @Column(length = 10, nullable = false)
    @Schema(description = "Categoria do curso", example = "BACK", maxLength = 10)
    private String category;

}