package com.example.project_exemple.main.service;

import com.example.project_exemple.main.exception.ItemNotFoundException;
import com.example.project_exemple.main.model.Course;
import com.example.project_exemple.main.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository repository;

    public List<Course> listar() {
        log.info("Listando cursos");
        return repository.findAll();
    }

    public Course salvar(Course course) {
        log.info("Salvando curso: " + course.toString());
        return repository.save(course);
    }

    public void deletar(Long id) {
        log.info("Deletando curso com id: " + id);

        var record = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Curso não encontrado com id: " + id));

        repository.deleteById(record.getId());
    }

    public Course atualizar(Long id,Course course) {
        log.info("Atualizando curso: " + course.toString());

        return repository.findById(id).map(record -> {
            record.setName(course.getName());
            record.setCategory(course.getCategory());
            return repository.save(record);
        }).orElseThrow(() -> new ItemNotFoundException("Curso não encontrado com id: " + course.getId()));
    }
}
