package com.br.courses.mapper;

import com.br.courses.dto.CourseRequest;
import com.br.courses.dto.CourseResponse;
import com.br.courses.model.Course;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para converter entidades Course em DTOs CourseResponse
 * e requisições CourseRequest em entidades Course
 */
@Component
public class CourseMapper {

    /**
     * Converte uma entidade Course para CourseResponse
     *
     * @param course entidade Course a ser convertida
     * @return CourseResponse ou null se course for null
     */
    public CourseResponse toResponse(Course course) {
        if (course == null) {
            return null;
        }
        return new CourseResponse(
            course.getId(),
            course.getTitle(),
            course.getDescription(),
            course.getCategory(),
            course.getDuration()
        );
    }

    /**
     * Converte uma lista de Course para lista de CourseResponse
     *
     * @param courses lista de Course a ser convertida
     * @return lista de CourseResponse
     */
    public List<CourseResponse> toResponseList(List<Course> courses) {
        if (courses == null) {
            return List.of();
        }
        return courses.stream()
            .map(this::toResponse)
            .toList();
    }

    /**
     * Converte um CourseRequest em uma entidade Course
     *
     * @param request CourseRequest a ser convertida
     * @return Course nova ou null se request for null
     */
    public Course toEntity(CourseRequest request) {
        if (request == null) {
            return null;
        }
        Course course = new Course();
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCategory(request.category());
        course.setDuration(request.duration());
        return course;
    }

    /**
     * Atualiza uma entidade Course existente com os dados de um CourseRequest
     *
     * @param request CourseRequest com os novos dados
     * @param course Course existente a ser atualizada
     * @return Course atualizado
     */
    public Course updateEntity(CourseRequest request, Course course) {
        if (request == null || course == null) {
            return course;
        }
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCategory(request.category());
        course.setDuration(request.duration());
        return course;
    }
}

