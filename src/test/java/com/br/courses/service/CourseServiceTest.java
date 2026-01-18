package com.br.courses.service;

import com.br.courses.dto.CourseRequest;
import com.br.courses.exception.ItemNotFoundException;
import com.br.courses.model.Course;
import com.br.courses.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Tests")
class CourseServiceTest {

    @Mock
    private CourseRepository repository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;
    private CourseRequest courseRequest;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Spring Boot Fundamentals");
        testCourse.setDescription("Aprenda os fundamentos do Spring Boot");
        testCourse.setCategory("Backend");
        testCourse.setDuration(40);

        courseRequest = new CourseRequest(
                "Spring Boot Advanced",
                "Aprenda tópicos avançados do Spring Boot",
                "Backend",
                50
        );
    }

    // ================== FIND ALL TESTS ==================

    @Test
    @DisplayName("Deve listar todos os cursos com sucesso")
    void testFindAllSuccess() {
        // Arrange
        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Java Basics");
        course2.setDescription("Fundamentos de Java");
        course2.setCategory("Backend");
        course2.setDuration(30);

        List<Course> courses = Arrays.asList(testCourse, course2);
        when(repository.findAll()).thenReturn(courses);

        // Act
        List<Course> result = courseService.findAll();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testCourse, course2);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há cursos")
    void testFindAllEmpty() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Course> result = courseService.findAll();

        // Assert
        assertThat(result).isEmpty();
        verify(repository, times(1)).findAll();
    }

    // ================== SAVE TESTS ==================

    @Test
    @DisplayName("Deve salvar um novo curso com sucesso")
    void testSaveSuccess() {
        // Arrange
        Course newCourse = new Course();
        newCourse.setTitle("New Course");
        newCourse.setDescription("Description");
        newCourse.setCategory("Category");
        newCourse.setDuration(20);

        when(repository.save(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            course.setId(1L);
            return course;
        });

        // Act
        Course result = courseService.save(newCourse);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New Course");
        verify(repository, times(1)).save(any(Course.class));
    }

    // ================== FIND BY ID TESTS ==================

    @Test
    @DisplayName("Deve encontrar um curso pelo ID")
    void testFindSuccess() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(testCourse));

        // Act
        Course result = courseService.find(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Spring Boot Fundamentals");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando curso não encontrado")
    void testFindNotFound() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> courseService.find(999L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Curso não encontrado com id: 999");

        verify(repository, times(1)).findById(999L);
    }

    // ================== UPDATE TESTS ==================

    @Test
    @DisplayName("Deve atualizar um curso com sucesso")
    void testUpdateSuccess() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(repository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        Course result = courseService.update(1L, courseRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Deve atualizar todos os campos do curso")
    void testUpdateAllFields() {
        // Arrange
        Course courseToUpdate = new Course();
        courseToUpdate.setId(1L);
        courseToUpdate.setTitle("Old Title");
        courseToUpdate.setDescription("Old Description");
        courseToUpdate.setCategory("Old Category");
        courseToUpdate.setDuration(10);

        when(repository.findById(1L)).thenReturn(Optional.of(courseToUpdate));
        when(repository.save(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            assertThat(course.getTitle()).isEqualTo("Spring Boot Advanced");
            assertThat(course.getDescription()).isEqualTo("Aprenda tópicos avançados do Spring Boot");
            assertThat(course.getCategory()).isEqualTo("Backend");
            assertThat(course.getDuration()).isEqualTo(50);
            return course;
        });

        // Act
        Course result = courseService.update(1L, courseRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar atualizar curso inexistente")
    void testUpdateNotFound() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> courseService.update(999L, courseRequest))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Curso não encontrado com id: 999");

        verify(repository, times(1)).findById(999L);
        verify(repository, never()).save(any());
    }

    // ================== DELETE TESTS ==================

    @Test
    @DisplayName("Deve deletar um curso com sucesso")
    void testDeleteSuccess() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(testCourse));
        doNothing().when(repository).deleteById(1L);

        // Act
        courseService.delete(1L);

        // Assert
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar deletar curso inexistente")
    void testDeleteNotFound() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> courseService.delete(999L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Curso não encontrado com id: 999");

        verify(repository, times(1)).findById(999L);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve chamar deleteById com o ID correto")
    void testDeleteCallsRepositoryWithCorrectId() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(testCourse));
        doNothing().when(repository).deleteById(1L);

        // Act
        courseService.delete(1L);

        // Assert
        verify(repository, times(1)).deleteById(1L);
    }

    // ================== EDGE CASES TESTS ==================

    @Test
    @DisplayName("Deve lidar com cursos com valores extremos")
    void testFindWithExtremeDuration() {
        // Arrange
        Course extremeCourse = new Course();
        extremeCourse.setId(1L);
        extremeCourse.setTitle("Extreme Course");
        extremeCourse.setDescription("Description");
        extremeCourse.setCategory("Category");
        extremeCourse.setDuration(Integer.MAX_VALUE);

        when(repository.findById(1L)).thenReturn(Optional.of(extremeCourse));

        // Act
        Course result = courseService.find(1L);

        // Assert
        assertThat(result.getDuration()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Deve lidar com títulos longos")
    void testSaveCourseWithLongTitle() {
        // Arrange
        Course courseWithLongTitle = new Course();
        courseWithLongTitle.setTitle("A".repeat(200));
        courseWithLongTitle.setDescription("Description");
        courseWithLongTitle.setCategory("Category");
        courseWithLongTitle.setDuration(20);

        when(repository.save(any(Course.class))).thenReturn(courseWithLongTitle);

        // Act
        Course result = courseService.save(courseWithLongTitle);

        // Assert
        assertThat(result.getTitle()).hasSize(200);
    }
}

