package com.example.sepm_assignment.repository;

import com.example.sepm_assignment.model.Student;
import com.example.sepm_assignment.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository Tests for StudentRepository
 * Testing Strategy: Use @DataJpaTest with H2 in-memory database
 * Testing custom query methods and JPA operations
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("StudentRepository JPA Tests")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Teacher testTeacher;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        // Clean up
        studentRepository.deleteAll();
        teacherRepository.deleteAll();

        // Create test teacher
        testTeacher = new Teacher();
        testTeacher.setName("Dr. Repository Test");
        testTeacher.setEmail("repo.test@school.com");
        testTeacher.setDepartment("Computer Science");
        testTeacher.setStudents(new ArrayList<>());
        testTeacher.setCourses(new ArrayList<>());
        testTeacher = teacherRepository.save(testTeacher);

        // Create test student
        testStudent = new Student();
        testStudent.setName("Repository Test Student");
        testStudent.setEmail("repo.student@student.com");
        testStudent.setStudentId("REPO-STU-001");
        testStudent.setTeacher(testTeacher);
        testStudent = studentRepository.save(testStudent);

        // Flush and clear to ensure persistence
        entityManager.flush();
        entityManager.clear();
    }

    // ==================== save() Tests ====================

    @Test
    @DisplayName("Should save student successfully")
    void testSave_Success() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setName("New Repository Student");
        newStudent.setEmail("new.repo.student@student.com");
        newStudent.setStudentId("REPO-STU-002");
        newStudent.setTeacher(testTeacher);

        // Act
        Student savedStudent = studentRepository.save(newStudent);
        entityManager.flush();

        // Assert
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getName()).isEqualTo("New Repository Student");
        assertThat(savedStudent.getEmail()).isEqualTo("new.repo.student@student.com");
        assertThat(savedStudent.getStudentId()).isEqualTo("REPO-STU-002");
    }

    @Test
    @DisplayName("Should persist student with relationships")
    void testSave_WithRelationships() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setName("Student with Teacher");
        newStudent.setEmail("student.with.teacher@student.com");
        newStudent.setStudentId("REPO-STU-003");
        newStudent.setTeacher(testTeacher);

        // Act
        Student savedStudent = studentRepository.save(newStudent);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Student foundStudent = studentRepository.findById(savedStudent.getId()).orElse(null);
        assertThat(foundStudent).isNotNull();
        assertThat(foundStudent.getTeacher()).isNotNull();
        assertThat(foundStudent.getTeacher().getId()).isEqualTo(testTeacher.getId());
    }

    // ==================== findById() Tests ====================

    @Test
    @DisplayName("Should find student by ID successfully")
    void testFindById_Success() {
        // Act
        Optional<Student> foundStudent = studentRepository.findById(testStudent.getId());

        // Assert
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getId()).isEqualTo(testStudent.getId());
        assertThat(foundStudent.get().getName()).isEqualTo("Repository Test Student");
        assertThat(foundStudent.get().getEmail()).isEqualTo("repo.student@student.com");
    }

    @Test
    @DisplayName("Should return empty Optional when student not found by ID")
    void testFindById_NotFound() {
        // Act
        Optional<Student> foundStudent = studentRepository.findById(99999L);

        // Assert
        assertThat(foundStudent).isEmpty();
    }

    // ==================== findAll() Tests ====================

    @Test
    @DisplayName("Should find all students successfully")
    void testFindAll_Success() {
        // Arrange
        Student student2 = new Student();
        student2.setName("Second Student");
        student2.setEmail("second.student@student.com");
        student2.setStudentId("REPO-STU-004");
        student2.setTeacher(testTeacher);
        studentRepository.save(student2);
        entityManager.flush();

        // Act
        List<Student> allStudents = studentRepository.findAll();

        // Assert
        assertThat(allStudents).isNotEmpty();
        assertThat(allStudents).hasSizeGreaterThanOrEqualTo(2);
    }

    // ==================== findByTeacherId() Tests ====================

    @Test
    @DisplayName("Should find students by teacher ID successfully")
    void testFindByTeacherId_Success() {
        // Arrange
        Student student2 = new Student();
        student2.setName("Second Student Under Teacher");
        student2.setEmail("second.under.teacher@student.com");
        student2.setStudentId("REPO-STU-005");
        student2.setTeacher(testTeacher);
        studentRepository.save(student2);
        entityManager.flush();

        // Act
        List<Student> studentsUnderTeacher = studentRepository.findByTeacherId(testTeacher.getId());

        // Assert
        assertThat(studentsUnderTeacher).isNotEmpty();
        assertThat(studentsUnderTeacher).hasSizeGreaterThanOrEqualTo(2);
        assertThat(studentsUnderTeacher).allMatch(s -> s.getTeacher().getId().equals(testTeacher.getId()));
    }

    @Test
    @DisplayName("Should return empty list when teacher has no students")
    void testFindByTeacherId_NoStudents() {
        // Arrange
        Teacher emptyTeacher = new Teacher();
        emptyTeacher.setName("Empty Teacher");
        emptyTeacher.setEmail("empty.teacher@school.com");
        emptyTeacher.setDepartment("Mathematics");
        emptyTeacher.setStudents(new ArrayList<>());
        emptyTeacher.setCourses(new ArrayList<>());
        emptyTeacher = teacherRepository.save(emptyTeacher);
        entityManager.flush();

        // Act
        List<Student> students = studentRepository.findByTeacherId(emptyTeacher.getId());

        // Assert
        assertThat(students).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list for non-existent teacher ID")
    void testFindByTeacherId_NonExistentTeacher() {
        // Act
        List<Student> students = studentRepository.findByTeacherId(99999L);

        // Assert
        assertThat(students).isEmpty();
    }

    // ==================== findByEmail() Tests ====================

    @Test
    @DisplayName("Should find student by email successfully")
    void testFindByEmail_Success() {
        // Act
        Optional<Student> foundStudent = studentRepository.findByEmail("repo.student@student.com");

        // Assert
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getEmail()).isEqualTo("repo.student@student.com");
        assertThat(foundStudent.get().getName()).isEqualTo("Repository Test Student");
    }

    @Test
    @DisplayName("Should return empty Optional when email not found")
    void testFindByEmail_NotFound() {
        // Act
        Optional<Student> foundStudent = studentRepository.findByEmail("nonexistent@student.com");

        // Assert
        assertThat(foundStudent).isEmpty();
    }

    @Test
    @DisplayName("Should handle case-sensitive email search")
    void testFindByEmail_CaseSensitive() {
        // Act
        Optional<Student> foundStudent = studentRepository.findByEmail("REPO.STUDENT@STUDENT.COM");

        // Assert
        // Depending on database collation, this might be empty
        // For H2, emails are typically case-sensitive
        assertThat(foundStudent).isEmpty();
    }

    // ==================== findByStudentId() Tests ====================

    @Test
    @DisplayName("Should find student by student ID successfully")
    void testFindByStudentId_Success() {
        // Act
        Optional<Student> foundStudent = studentRepository.findByStudentId("REPO-STU-001");

        // Assert
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getStudentId()).isEqualTo("REPO-STU-001");
        assertThat(foundStudent.get().getName()).isEqualTo("Repository Test Student");
    }

    @Test
    @DisplayName("Should return empty Optional when student ID not found")
    void testFindByStudentId_NotFound() {
        // Act
        Optional<Student> foundStudent = studentRepository.findByStudentId("NON-EXISTENT-ID");

        // Assert
        assertThat(foundStudent).isEmpty();
    }

    // ==================== deleteById() Tests ====================

    @Test
    @DisplayName("Should delete student by ID successfully")
    void testDeleteById_Success() {
        // Arrange
        Long studentId = testStudent.getId();

        // Act
        studentRepository.deleteById(studentId);
        entityManager.flush();

        // Assert
        Optional<Student> deletedStudent = studentRepository.findById(studentId);
        assertThat(deletedStudent).isEmpty();
    }

    @Test
    @DisplayName("Should handle deletion of non-existent student gracefully")
    void testDeleteById_NonExistent() {
        // Act & Assert - JPA doesn't throw exception for deleting non-existent entity
        studentRepository.deleteById(99999L);
        entityManager.flush();
    }

    // ==================== Update Tests ====================

    @Test
    @DisplayName("Should update student successfully")
    void testUpdate_Success() {
        // Arrange
        Student studentToUpdate = studentRepository.findById(testStudent.getId()).orElseThrow();
        studentToUpdate.setName("Updated Student Name");
        studentToUpdate.setEmail("updated.email@student.com");
        studentToUpdate.setStudentId("UPDATED-ID");

        // Act
        Student updatedStudent = studentRepository.save(studentToUpdate);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Student foundStudent = studentRepository.findById(updatedStudent.getId()).orElseThrow();
        assertThat(foundStudent.getName()).isEqualTo("Updated Student Name");
        assertThat(foundStudent.getEmail()).isEqualTo("updated.email@student.com");
        assertThat(foundStudent.getStudentId()).isEqualTo("UPDATED-ID");
    }

    // ==================== Transactional Behavior Tests ====================

    @Test
    @DisplayName("Should maintain referential integrity with teacher")
    void testReferentialIntegrity() {
        // Arrange & Act
        Student foundStudent = studentRepository.findById(testStudent.getId()).orElseThrow();

        // Assert
        assertThat(foundStudent.getTeacher()).isNotNull();
        assertThat(foundStudent.getTeacher().getId()).isEqualTo(testTeacher.getId());
        assertThat(foundStudent.getTeacher().getName()).isEqualTo("Dr. Repository Test");
    }

    @Test
    @DisplayName("Should count students correctly")
    void testCount() {
        // Arrange
        long initialCount = studentRepository.count();

        Student newStudent = new Student();
        newStudent.setName("Count Test Student");
        newStudent.setEmail("count.test@student.com");
        newStudent.setStudentId("COUNT-001");
        newStudent.setTeacher(testTeacher);
        studentRepository.save(newStudent);
        entityManager.flush();

        // Act
        long newCount = studentRepository.count();

        // Assert
        assertThat(newCount).isEqualTo(initialCount + 1);
    }

    @Test
    @DisplayName("Should check if student exists by ID")
    void testExistsById() {
        // Act
        boolean exists = studentRepository.existsById(testStudent.getId());
        boolean notExists = studentRepository.existsById(99999L);

        // Assert
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Should handle multiple students with same teacher")
    void testMultipleStudentsSameTeacher() {
        // Arrange
        Student student2 = new Student();
        student2.setName("Student Two");
        student2.setEmail("student.two@student.com");
        student2.setStudentId("STU-TWO");
        student2.setTeacher(testTeacher);

        Student student3 = new Student();
        student3.setName("Student Three");
        student3.setEmail("student.three@student.com");
        student3.setStudentId("STU-THREE");
        student3.setTeacher(testTeacher);

        // Act
        studentRepository.save(student2);
        studentRepository.save(student3);
        entityManager.flush();

        List<Student> students = studentRepository.findByTeacherId(testTeacher.getId());

        // Assert
        assertThat(students).hasSizeGreaterThanOrEqualTo(3);
    }
}

