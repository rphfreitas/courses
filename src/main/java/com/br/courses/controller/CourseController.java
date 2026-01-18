package com.br.courses.controller;

import com.br.courses.dto.CourseRequest;
import com.br.courses.dto.CourseResponse;
import com.br.courses.mapper.CourseMapper;
import com.br.courses.model.Course;
import com.br.courses.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "Endpoints para gerenciamento de cursos")
public class CourseController {

    private final CourseService service;
    private final CourseMapper courseMapper;

    @GetMapping
    @Operation(summary = "Listar todos os cursos", description = "Retorna uma lista de todos os cursos cadastrados no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponse.class)))
    public @ResponseBody List<CourseResponse> listarTodos() {
        return courseMapper.toResponseList(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar curso por ID", description = "Retorna um curso específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado e retornado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado com o ID fornecido",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "ID único do curso", required = true, example = "1")
    public @ResponseBody ResponseEntity<CourseResponse> listar(@PathVariable Long id) {
        return ResponseEntity.ok(courseMapper.toResponse(service.find(id)));
    }

    @PostMapping
    @Operation(summary = "Criar novo curso", description = "Cria um novo curso no sistema com validação de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou incompletos",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<CourseResponse> salvar(@RequestBody @Valid CourseRequest courseRequest) {
        Course course = courseMapper.toEntity(courseRequest);
        Course savedCourse = service.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseMapper.toResponse(savedCourse));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar curso", description = "Atualiza um curso existente com novos dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado com o ID fornecido",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "ID único do curso a atualizar", required = true, example = "1")
    public @ResponseBody ResponseEntity<CourseResponse> editar(@PathVariable Long id, @RequestBody @Valid CourseRequest courseRequest) {
        Course course = service.find(id);

        Course savedCourse = service.update(course.getId(), courseRequest);
        return ResponseEntity.ok(courseMapper.toResponse(savedCourse));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar curso", description = "Remove um curso do sistema pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado com o ID fornecido",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "ID único do curso a deletar", required = true, example = "1")
    public @ResponseBody ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
