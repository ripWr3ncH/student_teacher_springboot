package com.example.sepm_assignment.service;

import com.example.sepm_assignment.model.Teacher;
import com.example.sepm_assignment.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for TeacherService
 * Testing Strategy: AAA Pattern (Arrange-Act-Assert)
 * Using Mockito to mock repository dependencies
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TeacherService Unit Tests")
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        // Arrange: Set up test data
        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setName("Dr. John Smith");
        testTeacher.setEmail("john.smith@school.com");
        testTeacher.setDepartment("Computer Science");
        testTeacher.setStudents(new ArrayList<>());
        testTeacher.setCourses(new ArrayList<>());
    }

    // ==================== findAll() Tests ====================

    @Test
    @DisplayName("Should return all teachers successfully")
    void testFindAll_Success() {
        // Arrange
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setName("Dr. Jane Doe");
        teacher2.setEmail("jane.doe@school.com");
        teacher2.setDepartment("Mathematics");
        teacher2.setStudents(new ArrayList<>());
        teacher2.setCourses(new ArrayList<>());

        List<Teacher> expectedTeachers = Arrays.asList(testTeacher, teacher2);
        when(teacherRepository.findAll()).thenReturn(expectedTeachers);

        // Act
        List<Teacher> actualTeachers = teacherService.findAll();

        // Assert
        assertThat(actualTeachers).isNotNull();
        assertThat(actualTeachers).hasSize(2);
        assertThat(actualTeachers).containsExactlyInAnyOrder(testTeacher, teacher2);
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no teachers exist")
    void testFindAll_EmptyList() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Teacher> actualTeachers = teacherService.findAll();

        // Assert
        assertThat(actualTeachers).isNotNull();
        assertThat(actualTeachers).isEmpty();
        verify(teacherRepository, times(1)).findAll();
    }

    // ==================== findById() Tests ====================

    @Test
    @DisplayName("Should find teacher by ID successfully")
    void testFindById_Success() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));

        // Act
        Optional<Teacher> foundTeacher = teacherService.findById(1L);

        // Assert
        assertThat(foundTeacher).isPresent();
        assertThat(foundTeacher.get().getId()).isEqualTo(1L);
        assertThat(foundTeacher.get().getName()).isEqualTo("Dr. John Smith");
        assertThat(foundTeacher.get().getEmail()).isEqualTo("john.smith@school.com");
        assertThat(foundTeacher.get().getDepartment()).isEqualTo("Computer Science");
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty Optional when teacher not found by ID")
    void testFindById_NotFound() {
        // Arrange
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Teacher> foundTeacher = teacherService.findById(999L);

        // Assert
        assertThat(foundTeacher).isEmpty();
        verify(teacherRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should throw exception when ID is null")
    void testFindById_NullId() {
        // Arrange
        when(teacherRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        // Act & Assert
        assertThatThrownBy(() -> teacherService.findById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID cannot be null");
    }

    // ==================== findByEmail() Tests ====================

    @Test
    @DisplayName("Should find teacher by email successfully")
    void testFindByEmail_Success() {
        // Arrange
        when(teacherRepository.findByEmail("john.smith@school.com")).thenReturn(Optional.of(testTeacher));

        // Act
        Optional<Teacher> foundTeacher = teacherService.findByEmail("john.smith@school.com");

        // Assert
        assertThat(foundTeacher).isPresent();
        assertThat(foundTeacher.get().getEmail()).isEqualTo("john.smith@school.com");
        assertThat(foundTeacher.get().getName()).isEqualTo("Dr. John Smith");
        verify(teacherRepository, times(1)).findByEmail("john.smith@school.com");
    }

    @Test
    @DisplayName("Should return empty Optional when teacher not found by email")
    void testFindByEmail_NotFound() {
        // Arrange
        when(teacherRepository.findByEmail("nonexistent@school.com")).thenReturn(Optional.empty());

        // Act
        Optional<Teacher> foundTeacher = teacherService.findByEmail("nonexistent@school.com");

        // Assert
        assertThat(foundTeacher).isEmpty();
        verify(teacherRepository, times(1)).findByEmail("nonexistent@school.com");
    }

    @Test
    @DisplayName("Should handle null email gracefully")
    void testFindByEmail_NullEmail() {
        // Arrange
        when(teacherRepository.findByEmail(null)).thenReturn(Optional.empty());

        // Act
        Optional<Teacher> foundTeacher = teacherService.findByEmail(null);

        // Assert
        assertThat(foundTeacher).isEmpty();
        verify(teacherRepository, times(1)).findByEmail(null);
    }

    @Test
    @DisplayName("Should handle case-sensitive email search")
    void testFindByEmail_CaseSensitive() {
        // Arrange
        when(teacherRepository.findByEmail("JOHN.SMITH@SCHOOL.COM")).thenReturn(Optional.empty());

        // Act
        Optional<Teacher> foundTeacher = teacherService.findByEmail("JOHN.SMITH@SCHOOL.COM");

        // Assert
        assertThat(foundTeacher).isEmpty();
        verify(teacherRepository, times(1)).findByEmail("JOHN.SMITH@SCHOOL.COM");
    }

    // ==================== save() Tests ====================

    @Test
    @DisplayName("Should save teacher successfully")
    void testSave_Success() {
        // Arrange
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // Act
        Teacher savedTeacher = teacherService.save(testTeacher);

        // Assert
        assertThat(savedTeacher).isNotNull();
        assertThat(savedTeacher.getId()).isEqualTo(1L);
        assertThat(savedTeacher.getName()).isEqualTo("Dr. John Smith");
        assertThat(savedTeacher.getDepartment()).isEqualTo("Computer Science");
        verify(teacherRepository, times(1)).save(testTeacher);
    }

    @Test
    @DisplayName("Should save new teacher without ID")
    void testSave_NewTeacher() {
        // Arrange
        Teacher newTeacher = new Teacher();
        newTeacher.setName("Dr. New Teacher");
        newTeacher.setEmail("new.teacher@school.com");
        newTeacher.setDepartment("Physics");
        newTeacher.setStudents(new ArrayList<>());
        newTeacher.setCourses(new ArrayList<>());

        Teacher savedTeacher = new Teacher();
        savedTeacher.setId(10L);
        savedTeacher.setName(newTeacher.getName());
        savedTeacher.setEmail(newTeacher.getEmail());
        savedTeacher.setDepartment(newTeacher.getDepartment());
        savedTeacher.setStudents(new ArrayList<>());
        savedTeacher.setCourses(new ArrayList<>());

        when(teacherRepository.save(newTeacher)).thenReturn(savedTeacher);

        // Act
        Teacher result = teacherService.save(newTeacher);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getName()).isEqualTo("Dr. New Teacher");
        verify(teacherRepository, times(1)).save(newTeacher);
    }

    // ==================== update() Tests ====================

    @Test
    @DisplayName("Should update teacher successfully")
    void testUpdate_Success() {
        // Arrange
        Teacher updatedDetails = new Teacher();
        updatedDetails.setName("Dr. John Smith Updated");
        updatedDetails.setEmail("john.updated@school.com");
        updatedDetails.setDepartment("Software Engineering");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // Act
        Teacher updatedTeacher = teacherService.update(1L, updatedDetails);

        // Assert
        assertThat(updatedTeacher).isNotNull();
        assertThat(updatedTeacher.getName()).isEqualTo("Dr. John Smith Updated");
        assertThat(updatedTeacher.getEmail()).isEqualTo("john.updated@school.com");
        assertThat(updatedTeacher.getDepartment()).isEqualTo("Software Engineering");
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).save(testTeacher);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent teacher")
    void testUpdate_TeacherNotFound() {
        // Arrange
        Teacher updatedDetails = new Teacher();
        updatedDetails.setName("Non-existent Teacher");

        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> teacherService.update(999L, updatedDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Teacher not found with id: 999");
        verify(teacherRepository, times(1)).findById(999L);
        verify(teacherRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle partial update correctly")
    void testUpdate_PartialUpdate() {
        // Arrange
        Teacher partialUpdate = new Teacher();
        partialUpdate.setName("Dr. John Smith Jr.");
        partialUpdate.setEmail(testTeacher.getEmail()); // Keep same email
        partialUpdate.setDepartment(testTeacher.getDepartment()); // Keep same department

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // Act
        Teacher updatedTeacher = teacherService.update(1L, partialUpdate);

        // Assert
        assertThat(updatedTeacher).isNotNull();
        assertThat(updatedTeacher.getName()).isEqualTo("Dr. John Smith Jr.");
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).save(testTeacher);
    }

    @Test
    @DisplayName("Should update teacher with same values")
    void testUpdate_SameValues() {
        // Arrange
        Teacher sameDetails = new Teacher();
        sameDetails.setName(testTeacher.getName());
        sameDetails.setEmail(testTeacher.getEmail());
        sameDetails.setDepartment(testTeacher.getDepartment());

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // Act
        Teacher updatedTeacher = teacherService.update(1L, sameDetails);

        // Assert
        assertThat(updatedTeacher).isNotNull();
        assertThat(updatedTeacher.getName()).isEqualTo(testTeacher.getName());
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).save(testTeacher);
    }

    // ==================== delete() Tests ====================

    @Test
    @DisplayName("Should delete teacher successfully")
    void testDelete_Success() {
        // Arrange
        doNothing().when(teacherRepository).deleteById(1L);

        // Act
        teacherService.delete(1L);

        // Assert
        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle deletion of non-existent teacher")
    void testDelete_NonExistent() {
        // Arrange
        doThrow(new RuntimeException("Teacher not found")).when(teacherRepository).deleteById(999L);

        // Act & Assert
        assertThatThrownBy(() -> teacherService.delete(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Teacher not found");
        verify(teacherRepository, times(1)).deleteById(999L);
    }

    // ==================== Edge Cases & Exception Handling ====================

    @Test
    @DisplayName("Should handle null teacher in save operation")
    void testSave_NullTeacher() {
        // Arrange
        when(teacherRepository.save(null)).thenThrow(new IllegalArgumentException("Teacher cannot be null"));

        // Act & Assert
        assertThatThrownBy(() -> teacherService.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should handle database exception during save")
    void testSave_DatabaseException() {
        // Arrange
        when(teacherRepository.save(any(Teacher.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThatThrownBy(() -> teacherService.save(testTeacher))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database connection failed");
    }

    @Test
    @DisplayName("Should handle concurrent modification exception")
    void testUpdate_ConcurrentModification() {
        // Arrange
        Teacher updatedDetails = new Teacher();
        updatedDetails.setName("Updated Name");
        updatedDetails.setEmail("updated@school.com");
        updatedDetails.setDepartment("Updated Dept");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(teacherRepository.save(any(Teacher.class)))
                .thenThrow(new RuntimeException("Concurrent modification detected"));

        // Act & Assert
        assertThatThrownBy(() -> teacherService.update(1L, updatedDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Concurrent modification detected");
        verify(teacherRepository, times(1)).findById(1L);
    }

    // ==================== Business Logic Tests ====================

    @Test
    @DisplayName("Should maintain teacher-student relationship integrity")
    void testTeacherStudentRelationship() {
        // This test ensures that the teacher entity maintains its relationships
        // Arrange
        assertThat(testTeacher.getStudents()).isNotNull();
        assertThat(testTeacher.getCourses()).isNotNull();

        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // Act
        Teacher savedTeacher = teacherService.save(testTeacher);

        // Assert
        assertThat(savedTeacher.getStudents()).isNotNull();
        assertThat(savedTeacher.getCourses()).isNotNull();
    }
}

