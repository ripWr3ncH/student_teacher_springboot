package com.example.sepm_assignment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for Student Entity
 * Testing Strategy: Test getters, setters, equals, hashCode, and validation
 */
@DisplayName("Student Entity Tests")
class StudentTest {

    private Student student;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Dr. Test Teacher");
        teacher.setEmail("teacher@school.com");
        teacher.setDepartment("Computer Science");
        teacher.setStudents(new ArrayList<>());
        teacher.setCourses(new ArrayList<>());

        student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setEmail("student@student.com");
        student.setStudentId("STU001");
        student.setTeacher(teacher);
    }

    // ==================== Getter/Setter Tests ====================

    @Test
    @DisplayName("Should set and get ID correctly")
    void testIdGetterSetter() {
        // Arrange
        Student newStudent = new Student();

        // Act
        newStudent.setId(100L);

        // Assert
        assertThat(newStudent.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void testNameGetterSetter() {
        // Arrange
        Student newStudent = new Student();

        // Act
        newStudent.setName("John Doe");

        // Assert
        assertThat(newStudent.getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should set and get email correctly")
    void testEmailGetterSetter() {
        // Arrange
        Student newStudent = new Student();

        // Act
        newStudent.setEmail("john.doe@student.com");

        // Assert
        assertThat(newStudent.getEmail()).isEqualTo("john.doe@student.com");
    }

    @Test
    @DisplayName("Should set and get student ID correctly")
    void testStudentIdGetterSetter() {
        // Arrange
        Student newStudent = new Student();

        // Act
        newStudent.setStudentId("STU12345");

        // Assert
        assertThat(newStudent.getStudentId()).isEqualTo("STU12345");
    }

    @Test
    @DisplayName("Should set and get teacher correctly")
    void testTeacherGetterSetter() {
        // Arrange
        Student newStudent = new Student();
        Teacher newTeacher = new Teacher();
        newTeacher.setId(2L);
        newTeacher.setName("Dr. New Teacher");

        // Act
        newStudent.setTeacher(newTeacher);

        // Assert
        assertThat(newStudent.getTeacher()).isNotNull();
        assertThat(newStudent.getTeacher().getId()).isEqualTo(2L);
        assertThat(newStudent.getTeacher().getName()).isEqualTo("Dr. New Teacher");
    }

    // ==================== Constructor Tests ====================

    @Test
    @DisplayName("Should create student with no-args constructor")
    void testNoArgsConstructor() {
        // Act
        Student newStudent = new Student();

        // Assert
        assertThat(newStudent).isNotNull();
        assertThat(newStudent.getId()).isNull();
        assertThat(newStudent.getName()).isNull();
        assertThat(newStudent.getEmail()).isNull();
        assertThat(newStudent.getStudentId()).isNull();
        assertThat(newStudent.getTeacher()).isNull();
    }

    @Test
    @DisplayName("Should create student with all-args constructor")
    void testAllArgsConstructor() {
        // Act
        Student newStudent = new Student(2L, "Jane Smith", "jane.smith@student.com", "STU002", teacher);

        // Assert
        assertThat(newStudent.getId()).isEqualTo(2L);
        assertThat(newStudent.getName()).isEqualTo("Jane Smith");
        assertThat(newStudent.getEmail()).isEqualTo("jane.smith@student.com");
        assertThat(newStudent.getStudentId()).isEqualTo("STU002");
        assertThat(newStudent.getTeacher()).isEqualTo(teacher);
    }

    // ==================== Equals and HashCode Tests ====================

    @Test
    @DisplayName("Should be equal when all fields match")
    void testEquals_AllFieldsMatch() {
        // Arrange
        Student student1 = new Student(1L, "Test Student", "student@student.com", "STU001", teacher);
        Student student2 = new Student(1L, "Test Student", "student@student.com", "STU001", teacher);

        // Assert
        assertThat(student1).isEqualTo(student2);
        assertThat(student1.hashCode()).isEqualTo(student2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when IDs differ")
    void testEquals_DifferentIds() {
        // Arrange
        Student student1 = new Student(1L, "Test Student", "student@student.com", "STU001", teacher);
        Student student2 = new Student(2L, "Test Student", "student@student.com", "STU001", teacher);

        // Assert
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    @DisplayName("Should not be equal when names differ")
    void testEquals_DifferentNames() {
        // Arrange
        Student student1 = new Student(1L, "Student One", "student@student.com", "STU001", teacher);
        Student student2 = new Student(1L, "Student Two", "student@student.com", "STU001", teacher);

        // Assert
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    @DisplayName("Should handle null in equals")
    void testEquals_Null() {
        // Assert
        assertThat(student).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should be equal to itself")
    void testEquals_Self() {
        // Assert
        assertThat(student).isEqualTo(student);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void testEquals_DifferentClass() {
        // Arrange
        String notAStudent = "Not a Student";

        // Assert
        assertThat(student).isNotEqualTo(notAStudent);
    }

    // ==================== ToString Tests ====================

    @Test
    @DisplayName("Should generate toString with all fields")
    void testToString() {
        // Act
        String studentString = student.toString();

        // Assert
        assertThat(studentString).contains("Student");
        assertThat(studentString).contains("id=1");
        assertThat(studentString).contains("Test Student");
        assertThat(studentString).contains("student@student.com");
        assertThat(studentString).contains("STU001");
    }

    // ==================== Relationship Tests ====================

    @Test
    @DisplayName("Should maintain teacher relationship")
    void testTeacherRelationship() {
        // Assert
        assertThat(student.getTeacher()).isNotNull();
        assertThat(student.getTeacher().getName()).isEqualTo("Dr. Test Teacher");
        assertThat(student.getTeacher().getDepartment()).isEqualTo("Computer Science");
    }

    @Test
    @DisplayName("Should allow null teacher")
    void testNullTeacher() {
        // Arrange
        Student orphanStudent = new Student();
        orphanStudent.setName("Orphan Student");
        orphanStudent.setEmail("orphan@student.com");
        orphanStudent.setStudentId("ORPHAN001");

        // Assert
        assertThat(orphanStudent.getTeacher()).isNull();
    }

    @Test
    @DisplayName("Should update teacher relationship")
    void testUpdateTeacher() {
        // Arrange
        Teacher newTeacher = new Teacher();
        newTeacher.setId(3L);
        newTeacher.setName("Dr. New Advisor");
        newTeacher.setDepartment("Mathematics");
        newTeacher.setStudents(new ArrayList<>());
        newTeacher.setCourses(new ArrayList<>());

        // Act
        student.setTeacher(newTeacher);

        // Assert
        assertThat(student.getTeacher().getId()).isEqualTo(3L);
        assertThat(student.getTeacher().getName()).isEqualTo("Dr. New Advisor");
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Should handle empty string values")
    void testEmptyStringValues() {
        // Arrange
        Student emptyStudent = new Student();
        emptyStudent.setName("");
        emptyStudent.setEmail("");
        emptyStudent.setStudentId("");

        // Assert
        assertThat(emptyStudent.getName()).isEmpty();
        assertThat(emptyStudent.getEmail()).isEmpty();
        assertThat(emptyStudent.getStudentId()).isEmpty();
    }

    @Test
    @DisplayName("Should handle very long string values")
    void testLongStringValues() {
        // Arrange
        String longName = "A".repeat(255);
        String longEmail = "b".repeat(100) + "@student.com";
        String longStudentId = "STU" + "1".repeat(100);

        Student longStudent = new Student();
        longStudent.setName(longName);
        longStudent.setEmail(longEmail);
        longStudent.setStudentId(longStudentId);

        // Assert
        assertThat(longStudent.getName()).hasSize(255);
        assertThat(longStudent.getEmail()).contains("@student.com");
        assertThat(longStudent.getStudentId()).startsWith("STU");
    }

    @Test
    @DisplayName("Should handle special characters in fields")
    void testSpecialCharacters() {
        // Arrange
        Student specialStudent = new Student();
        specialStudent.setName("José María O'Brien");
        specialStudent.setEmail("jose.maria@student.com");
        specialStudent.setStudentId("STU-2024-001");

        // Assert
        assertThat(specialStudent.getName()).isEqualTo("José María O'Brien");
        assertThat(specialStudent.getEmail()).isEqualTo("jose.maria@student.com");
        assertThat(specialStudent.getStudentId()).isEqualTo("STU-2024-001");
    }

    @Test
    @DisplayName("Should support method chaining")
    void testMethodChaining() {
        // Arrange
        Student chainStudent = new Student();

        // Act
        chainStudent.setId(10L);
        chainStudent.setName("Chain Student");
        chainStudent.setEmail("chain@student.com");
        chainStudent.setStudentId("CHAIN001");

        // Assert
        assertThat(chainStudent.getId()).isEqualTo(10L);
        assertThat(chainStudent.getName()).isEqualTo("Chain Student");
        assertThat(chainStudent.getEmail()).isEqualTo("chain@student.com");
        assertThat(chainStudent.getStudentId()).isEqualTo("CHAIN001");
    }
}

