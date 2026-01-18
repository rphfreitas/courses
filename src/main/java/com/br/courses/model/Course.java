package com.br.courses.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Schema(name = "Course", description = "Modelo de dados para um curso")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("_id")
    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
    @Column(length = 200, nullable = false)
    @Schema(description = "Título do curso", example = "Spring Boot Fundamentals", maxLength = 200)
    private String title;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Descrição detalhada do curso", example = "Aprenda os fundamentos do Spring Boot")
    private String description;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(max = 50, message = "A categoria deve ter no máximo 50 caracteres")
    @Column(length = 50, nullable = false)
    @Schema(description = "Categoria do curso", example = "Backend", maxLength = 50)
    private String category;

    @Positive(message = "A duração deve ser maior que zero")
    @Column(nullable = false)
    @Schema(description = "Duração do curso em horas", example = "40")
    private Integer duration;

}