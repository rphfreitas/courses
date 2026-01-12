package com.example.project_exemple.main.controller;

import com.example.project_exemple.main.model.Course;
import com.example.project_exemple.main.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;

    @GetMapping
    public @ResponseBody List<Course> listar() {

        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Course> salvar(@RequestBody @Valid Course course) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(course));
    }

    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<Course> editar(@PathVariable Long id, @RequestBody Course course) {
        Course atualizar = service.atualizar(id, course);
        return ResponseEntity.ok(atualizar);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody <Void> ResponseEntity deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
