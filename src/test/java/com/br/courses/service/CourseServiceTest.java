package com.br.courses.service;

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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para CourseService")
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Course course2;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setName("Spring Boot Basics");
        course.setCategory("BACK");

        course2 = new Course();
        course2.setId(2L);
        course2.setName("Advanced Spring");
        course2.setCategory("BACK");
    }

    // ==================== findAll Tests ====================

    @Test
    @DisplayName("Deve retornar lista de cursos quando existem cursos no banco")
    void testFindAllWithCourses() {
        // Arrange
        List<Course> courses = Arrays.asList(course, course2);
        when(courseRepository.findAll()).thenReturn(courses);

        // Act
        List<Course> result = courseService.findAll();

        // Assert
        assertThat(result)
                .isNotEmpty()
                .hasSize(2)
                .contains(course, course2);

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem cursos no banco")
    void testFindAllWithoutCourses() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Course> result = courseService.findAll();

        // Assert
        assertThat(result).isEmpty();

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista com um único curso")
    void testFindAllWithSingleCourse() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));

        // Act
        List<Course> result = courseService.findAll();

        // Assert
        assertThat(result)
                .hasSize(1)
                .contains(course);

        verify(courseRepository, times(1)).findAll();
    }

    // ==================== save Tests ====================

    @Test
    @DisplayName("Deve salvar um curso com sucesso")
    void testSaveSuccess() {
        // Arrange
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        Course result = courseService.save(course);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(course)
                .extracting(Course::getId, Course::getName, Course::getCategory)
                .containsExactly(1L, "Spring Boot Basics", "BACK");

        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @DisplayName("Deve salvar um novo curso sem ID")
    void testSaveNewCourseWithoutId() {
        // Arrange
        Course newCourse = new Course();
        newCourse.setName("Java Basics");
        newCourse.setCategory("BACK");

        Course savedCourse = new Course();
        savedCourse.setId(3L);
        savedCourse.setName("Java Basics");
        savedCourse.setCategory("BACK");

        when(courseRepository.save(newCourse)).thenReturn(savedCourse);

        // Act
        Course result = courseService.save(newCourse);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Java Basics");

        verify(courseRepository, times(1)).save(newCourse);
    }

    @Test
    @DisplayName("Deve salvar múltiplos cursos")
    void testSaveMultipleCourses() {
        // Arrange
        when(courseRepository.save(any(Course.class)))
                .thenReturn(course)
                .thenReturn(course2);

        // Act
        Course result1 = courseService.save(course);
        Course result2 = courseService.save(course2);

        // Assert
        assertThat(result1).isEqualTo(course);
        assertThat(result2).isEqualTo(course2);

        verify(courseRepository, times(2)).save(any(Course.class));
    }

    // ==================== find Tests ====================

    @Test
    @DisplayName("Deve encontrar um curso pelo ID com sucesso")
    void testFindByIdSuccess() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        Course result = courseService.find(1L);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(course)
                .extracting(Course::getId, Course::getName, Course::getCategory)
                .containsExactly(1L, "Spring Boot Basics", "BACK");

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ItemNotFoundException quando curso não é encontrado")
    void testFindByIdNotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> courseService.find(999L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Curso não encontrado com id: 999");

        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve lançar ItemNotFoundException com mensagem correta")
    void testFindByIdNotFoundMessageValidation() {
        // Arrange
        Long nonExistentId = 100L;
        when(courseRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> courseService.find(nonExistentId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("100");

        verify(courseRepository, times(1)).findById(nonExistentId);
    }

    // ==================== delete Tests ====================

    @Test
    @DisplayName("Deve deletar um curso com sucesso")
    void testDeleteSuccess() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).deleteById(1L);

        // Act
        courseService.delete(1L);

        // Assert
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ItemNotFoundException ao tentar deletar curso inexistente")
    void testDeleteNotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> courseService.delete(999L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Curso não encontrado com id: 999");

        verify(courseRepository, times(1)).findById(999L);
        verify(courseRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve verificar que deleteById é chamado com o ID correto")
    void testDeleteVerifyCorrectIdDeleted() {
        // Arrange
        Long courseId = 5L;
        Course courseToDelete = new Course();
        courseToDelete.setId(courseId);
        courseToDelete.setName("Course to Delete");
        courseToDelete.setCategory("TEST");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseToDelete));
        doNothing().when(courseRepository).deleteById(courseId);

        // Act
        courseService.delete(courseId);

        // Assert
        verify(courseRepository).deleteById(courseId);
    }

    // ==================== update Tests ====================

    @Test
    @DisplayName("Deve atualizar um curso com sucesso")
    void testUpdateSuccess() {
        // Arrange
        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setName("Spring Boot Advanced");
        updatedCourse.setCategory("BACK");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        // Act
        Course result = courseService.update(1L, updatedCourse);

        // Assert
        assertThat(result)
                .isNotNull()
                .extracting(Course::getName, Course::getCategory)
                .containsExactly("Spring Boot Advanced", "BACK");

        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Deve lançar ItemNotFoundException ao atualizar curso inexistente")
    void testUpdateNotFound() {
        // Arrange
        Course updateData = new Course();
        updateData.setId(999L);
        updateData.setName("Nonexistent Course");
        updateData.setCategory("TEST");

        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> courseService.update(999L, updateData))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Curso não encontrado");

        verify(courseRepository, times(1)).findById(999L);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos fornecidos")
    void testUpdateOnlyProvidedFields() {
        // Arrange
        Course existingCourse = new Course();
        existingCourse.setId(1L);
        existingCourse.setName("Original Name");
        existingCourse.setCategory("FRONT");

        Course updateData = new Course();
        updateData.setName("Updated Name");
        updateData.setCategory("BACK");

        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setName("Updated Name");
        updatedCourse.setCategory("BACK");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        // Act
        Course result = courseService.update(1L, updateData);

        // Assert
        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getCategory()).isEqualTo("BACK");

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Deve manter o ID original durante atualização")
    void testUpdateMaintainsOriginalId() {
        // Arrange
        Course existingCourse = new Course();
        existingCourse.setId(1L);
        existingCourse.setName("Original");
        existingCourse.setCategory("BACK");

        Course updateData = new Course();
        updateData.setId(999L);
        updateData.setName("Updated");
        updateData.setCategory("FRONT");

        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setName("Updated");
        savedCourse.setCategory("FRONT");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // Act
        Course result = courseService.update(1L, updateData);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Deve lidar com curso com nome muito longo")
    void testSaveCourseWithLongName() {
        // Arrange
        Course longNameCourse = new Course();
        longNameCourse.setName("A".repeat(200));
        longNameCourse.setCategory("BACK");

        when(courseRepository.save(any(Course.class))).thenReturn(longNameCourse);

        // Act
        Course result = courseService.save(longNameCourse);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).hasSize(200);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Deve lidar com múltiplas chamadas ao repositório")
    void testMultipleRepositoryCalls() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course, course2));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        List<Course> allCourses = courseService.findAll();
        Course foundCourse = courseService.find(1L);
        Course savedCourse = courseService.save(course);

        // Assert
        assertThat(allCourses).hasSize(2);
        assertThat(foundCourse).isNotNull();
        assertThat(savedCourse).isNotNull();

        verify(courseRepository, times(1)).findAll();
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
}

