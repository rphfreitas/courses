package com.br.courses.controller;

import com.br.courses.model.Course;
import com.br.courses.service.CourseService;
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
    public @ResponseBody List<Course> listarTodos() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Course> listar(@PathVariable Long id) {
        return ResponseEntity.ok(service.find(id));
    }

    @PostMapping
    public ResponseEntity<Course> salvar(@RequestBody @Valid Course course) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(course));
    }

    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<Course> editar(@PathVariable Long id, @RequestBody Course course) {
        Course atualizar = service.update(id, course);
        return ResponseEntity.ok(atualizar);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
