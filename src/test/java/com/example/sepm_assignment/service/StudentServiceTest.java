package com.example.sepm_assignment.service;

import com.example.sepm_assignment.model.Student;
import com.example.sepm_assignment.model.Teacher;
import com.example.sepm_assignment.repository.StudentRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for StudentService
 * Testing Strategy: AAA Pattern (Arrange-Act-Assert)
 * Using Mockito to mock repository dependencies
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Unit Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
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

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Alice Johnson");
        testStudent.setEmail("alice.johnson@student.com");
        testStudent.setStudentId("STU001");
        testStudent.setTeacher(testTeacher);
    }

    // ==================== findAll() Tests ====================

    @Test
    @DisplayName("Should return all students successfully")
    void testFindAll_Success() {
        // Arrange
        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Bob Wilson");
        student2.setEmail("bob.wilson@student.com");
        student2.setStudentId("STU002");

        List<Student> expectedStudents = Arrays.asList(testStudent, student2);
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // Act
        List<Student> actualStudents = studentService.findAll();

        // Assert
        assertThat(actualStudents).isNotNull();
        assertThat(actualStudents).hasSize(2);
        assertThat(actualStudents).containsExactlyInAnyOrder(testStudent, student2);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no students exist")
    void testFindAll_EmptyList() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Student> actualStudents = studentService.findAll();

        // Assert
        assertThat(actualStudents).isNotNull();
        assertThat(actualStudents).isEmpty();
        verify(studentRepository, times(1)).findAll();
    }

    // ==================== findById() Tests ====================

    @Test
    @DisplayName("Should find student by ID successfully")
    void testFindById_Success() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // Act
        Optional<Student> foundStudent = studentService.findById(1L);

        // Assert
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getId()).isEqualTo(1L);
        assertThat(foundStudent.get().getName()).isEqualTo("Alice Johnson");
        assertThat(foundStudent.get().getEmail()).isEqualTo("alice.johnson@student.com");
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty Optional when student not found by ID")
    void testFindById_NotFound() {
        // Arrange
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Student> foundStudent = studentService.findById(999L);

        // Assert
        assertThat(foundStudent).isEmpty();
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should throw exception when ID is null")
    void testFindById_NullId() {
        // Arrange
        when(studentRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        // Act & Assert
        assertThatThrownBy(() -> studentService.findById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID cannot be null");
    }

    // ==================== findByTeacherId() Tests ====================

    @Test
    @DisplayName("Should find students by teacher ID successfully")
    void testFindByTeacherId_Success() {
        // Arrange
        List<Student> studentsUnderTeacher = Arrays.asList(testStudent);
        when(studentRepository.findByTeacherId(1L)).thenReturn(studentsUnderTeacher);

        // Act
        List<Student> foundStudents = studentService.findByTeacherId(1L);

        // Assert
        assertThat(foundStudents).isNotNull();
        assertThat(foundStudents).hasSize(1);
        assertThat(foundStudents.get(0).getTeacher().getId()).isEqualTo(1L);
        verify(studentRepository, times(1)).findByTeacherId(1L);
    }

    @Test
    @DisplayName("Should return empty list when teacher has no students")
    void testFindByTeacherId_NoStudents() {
        // Arrange
        when(studentRepository.findByTeacherId(1L)).thenReturn(new ArrayList<>());

        // Act
        List<Student> foundStudents = studentService.findByTeacherId(1L);

        // Assert
        assertThat(foundStudents).isNotNull();
        assertThat(foundStudents).isEmpty();
        verify(studentRepository, times(1)).findByTeacherId(1L);
    }

    // ==================== save() Tests ====================

    @Test
    @DisplayName("Should save student successfully")
    void testSave_Success() {
        // Arrange
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        Student savedStudent = studentService.save(testStudent);

        // Assert
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isEqualTo(1L);
        assertThat(savedStudent.getName()).isEqualTo("Alice Johnson");
        verify(studentRepository, times(1)).save(testStudent);
    }

    // ==================== saveWithTeacher() Tests ====================

    @Test
    @DisplayName("Should save student with teacher successfully")
    void testSaveWithTeacher_Success() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setName("Charlie Brown");
        newStudent.setEmail("charlie.brown@student.com");
        newStudent.setStudentId("STU003");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        // Act
        Student savedStudent = studentService.saveWithTeacher(1L, newStudent);

        // Assert
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getTeacher()).isEqualTo(testTeacher);
        verify(teacherRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    @DisplayName("Should throw exception when teacher not found for saveWithTeacher")
    void testSaveWithTeacher_TeacherNotFound() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setName("Charlie Brown");
        newStudent.setEmail("charlie.brown@student.com");
        newStudent.setStudentId("STU003");

        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.saveWithTeacher(999L, newStudent))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Teacher not found with id: 999");
        verify(teacherRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any());
    }

    // ==================== update() Tests ====================

    @Test
    @DisplayName("Should update student successfully")
    void testUpdate_Success() {
        // Arrange
        Student updatedDetails = new Student();
        updatedDetails.setName("Alice Johnson Updated");
        updatedDetails.setEmail("alice.updated@student.com");
        updatedDetails.setStudentId("STU001-UPDATED");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        Student updatedStudent = studentService.update(1L, updatedDetails);

        // Assert
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getName()).isEqualTo("Alice Johnson Updated");
        assertThat(updatedStudent.getEmail()).isEqualTo("alice.updated@student.com");
        assertThat(updatedStudent.getStudentId()).isEqualTo("STU001-UPDATED");
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(testStudent);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent student")
    void testUpdate_StudentNotFound() {
        // Arrange
        Student updatedDetails = new Student();
        updatedDetails.setName("Non-existent Student");

        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.update(999L, updatedDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Student not found with id: 999");
        verify(studentRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle partial update correctly")
    void testUpdate_PartialUpdate() {
        // Arrange
        Student partialUpdate = new Student();
        partialUpdate.setName("Alice Smith");
        partialUpdate.setEmail(testStudent.getEmail()); // Keep same email
        partialUpdate.setStudentId(testStudent.getStudentId()); // Keep same student ID

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        Student updatedStudent = studentService.update(1L, partialUpdate);

        // Assert
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getName()).isEqualTo("Alice Smith");
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(testStudent);
    }

    // ==================== delete() Tests ====================

    @Test
    @DisplayName("Should delete student successfully")
    void testDelete_Success() {
        // Arrange
        doNothing().when(studentRepository).deleteById(1L);

        // Act
        studentService.delete(1L);

        // Assert
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle deletion of non-existent student")
    void testDelete_NonExistent() {
        // Arrange
        doThrow(new RuntimeException("Student not found")).when(studentRepository).deleteById(999L);

        // Act & Assert
        assertThatThrownBy(() -> studentService.delete(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Student not found");
        verify(studentRepository, times(1)).deleteById(999L);
    }

    // ==================== Edge Cases & Exception Handling ====================

    @Test
    @DisplayName("Should handle null student in save operation")
    void testSave_NullStudent() {
        // Arrange
        when(studentRepository.save(null)).thenThrow(new IllegalArgumentException("Student cannot be null"));

        // Act & Assert
        assertThatThrownBy(() -> studentService.save(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should handle database exception during save")
    void testSave_DatabaseException() {
        // Arrange
        when(studentRepository.save(any(Student.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThatThrownBy(() -> studentService.save(testStudent))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database connection failed");
    }
}

