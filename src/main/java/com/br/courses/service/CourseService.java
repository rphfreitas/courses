package com.br.courses.service;

import com.br.courses.exception.ItemNotFoundException;
import com.br.courses.model.Course;
import com.br.courses.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository repository;

    public List<Course> findAll() {
        log.info("Listando cursos");
        return repository.findAll();
    }

    public Course save(Course course) {
        log.info("Salvando curso: " + course.toString());
        return repository.save(course);
    }

    public void delete(Long id) {
        log.info("Deletando curso com id: " + id);

        var record = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Curso não encontrado com id: " + id));

        repository.deleteById(record.getId());
    }

    public Course update(Long id, Course course) {
        log.info("Atualizando curso: " + course.toString());

        return repository.findById(id).map(record -> {
            record.setName(course.getName());
            record.setCategory(course.getCategory());
            return repository.save(record);
        }).orElseThrow(() -> new ItemNotFoundException("Curso não encontrado com id: " + course.getId()));
    }

    public Course find(Long id) {
        log.info("Listando curso com id: " + id);

        return repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Curso não encontrado com id: " + id));
    }
}
