package com.br.courses.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("_id")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
    @Column(length = 200, nullable = false)
    private String name;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(max = 10, message = "A categoria deve ter no máximo 10 caracteres")
    @Column(length = 10, nullable = false)
    private String category;

}