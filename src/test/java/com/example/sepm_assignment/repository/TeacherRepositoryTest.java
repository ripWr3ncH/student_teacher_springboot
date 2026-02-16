package com.example.sepm_assignment.repository;

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
 * Repository Tests for TeacherRepository
 * Testing Strategy: Use @DataJpaTest with H2 in-memory database
 * Testing custom query methods and JPA operations
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("TeacherRepository JPA Tests")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        // Clean up
        teacherRepository.deleteAll();

        // Create test teacher
        testTeacher = new Teacher();
        testTeacher.setName("Dr. Repository Test Teacher");
        testTeacher.setEmail("repo.test@school.com");
        testTeacher.setDepartment("Computer Science");
        testTeacher.setStudents(new ArrayList<>());
        testTeacher.setCourses(new ArrayList<>());
        testTeacher = teacherRepository.save(testTeacher);

        // Flush and clear to ensure persistence
        entityManager.flush();
        entityManager.clear();
    }

    // ==================== save() Tests ====================

    @Test
    @DisplayName("Should save teacher successfully")
    void testSave_Success() {
        // Arrange
        Teacher newTeacher = new Teacher();
        newTeacher.setName("Dr. New Teacher");
        newTeacher.setEmail("new.teacher@school.com");
        newTeacher.setDepartment("Mathematics");
        newTeacher.setStudents(new ArrayList<>());
        newTeacher.setCourses(new ArrayList<>());

        // Act
        Teacher savedTeacher = teacherRepository.save(newTeacher);
        entityManager.flush();

        // Assert
        assertThat(savedTeacher).isNotNull();
        assertThat(savedTeacher.getId()).isNotNull();
        assertThat(savedTeacher.getName()).isEqualTo("Dr. New Teacher");
        assertThat(savedTeacher.getEmail()).isEqualTo("new.teacher@school.com");
        assertThat(savedTeacher.getDepartment()).isEqualTo("Mathematics");
    }

    @Test
    @DisplayName("Should generate ID automatically")
    void testSave_AutoGenerateId() {
        // Arrange
        Teacher teacherWithoutId = new Teacher();
        teacherWithoutId.setName("Dr. Auto ID");
        teacherWithoutId.setEmail("auto.id@school.com");
        teacherWithoutId.setDepartment("Physics");
        teacherWithoutId.setStudents(new ArrayList<>());
        teacherWithoutId.setCourses(new ArrayList<>());

        // Act
        Teacher savedTeacher = teacherRepository.save(teacherWithoutId);
        entityManager.flush();

        // Assert
        assertThat(savedTeacher.getId()).isNotNull();
        assertThat(savedTeacher.getId()).isGreaterThan(0L);
    }

    // ==================== findById() Tests ====================

    @Test
    @DisplayName("Should find teacher by ID successfully")
    void testFindById_Success() {
        // Act
        Optional<Teacher> foundTeacher = teacherRepository.findById(testTeacher.getId());

        // Assert
        assertThat(foundTeacher).isPresent();
        assertThat(foundTeacher.get().getId()).isEqualTo(testTeacher.getId());
        assertThat(foundTeacher.get().getName()).isEqualTo("Dr. Repository Test Teacher");
        assertThat(foundTeacher.get().getEmail()).isEqualTo("repo.test@school.com");
        assertThat(foundTeacher.get().getDepartment()).isEqualTo("Computer Science");
    }

    @Test
    @DisplayName("Should return empty Optional when teacher not found by ID")
    void testFindById_NotFound() {
        // Act
        Optional<Teacher> foundTeacher = teacherRepository.findById(99999L);

        // Assert
        assertThat(foundTeacher).isEmpty();
    }

    // ==================== findAll() Tests ====================

    @Test
    @DisplayName("Should find all teachers successfully")
    void testFindAll_Success() {
        // Arrange
        Teacher teacher2 = new Teacher();
        teacher2.setName("Dr. Second Teacher");
        teacher2.setEmail("second.teacher@school.com");
        teacher2.setDepartment("Chemistry");
        teacher2.setStudents(new ArrayList<>());
        teacher2.setCourses(new ArrayList<>());
        teacherRepository.save(teacher2);
        entityManager.flush();

        // Act
        List<Teacher> allTeachers = teacherRepository.findAll();

        // Assert
        assertThat(allTeachers).isNotEmpty();
        assertThat(allTeachers).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should return empty list when no teachers exist")
    void testFindAll_Empty() {
        // Arrange
        teacherRepository.deleteAll();
        entityManager.flush();

        // Act
        List<Teacher> allTeachers = teacherRepository.findAll();

        // Assert
        assertThat(allTeachers).isEmpty();
    }

    // ==================== findByEmail() Tests ====================

    @Test
    @DisplayName("Should find teacher by email successfully")
    void testFindByEmail_Success() {
        // Act
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail("repo.test@school.com");

        // Assert
        assertThat(foundTeacher).isPresent();
        assertThat(foundTeacher.get().getEmail()).isEqualTo("repo.test@school.com");
        assertThat(foundTeacher.get().getName()).isEqualTo("Dr. Repository Test Teacher");
        assertThat(foundTeacher.get().getDepartment()).isEqualTo("Computer Science");
    }

    @Test
    @DisplayName("Should return empty Optional when email not found")
    void testFindByEmail_NotFound() {
        // Act
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail("nonexistent@school.com");

        // Assert
        assertThat(foundTeacher).isEmpty();
    }

    @Test
    @DisplayName("Should handle null email")
    void testFindByEmail_Null() {
        // Act
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail(null);

        // Assert
        assertThat(foundTeacher).isEmpty();
    }

    @Test
    @DisplayName("Should handle case-sensitive email search")
    void testFindByEmail_CaseSensitive() {
        // Act
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail("REPO.TEST@SCHOOL.COM");

        // Assert
        // For H2, emails are typically case-sensitive
        assertThat(foundTeacher).isEmpty();
    }

    @Test
    @DisplayName("Should find unique teacher by email")
    void testFindByEmail_Unique() {
        // Arrange
        Teacher teacher2 = new Teacher();
        teacher2.setName("Dr. Unique Email");
        teacher2.setEmail("unique.email@school.com");
        teacher2.setDepartment("Biology");
        teacher2.setStudents(new ArrayList<>());
        teacher2.setCourses(new ArrayList<>());
        teacherRepository.save(teacher2);
        entityManager.flush();

        // Act
        Optional<Teacher> foundTeacher1 = teacherRepository.findByEmail("repo.test@school.com");
        Optional<Teacher> foundTeacher2 = teacherRepository.findByEmail("unique.email@school.com");

        // Assert
        assertThat(foundTeacher1).isPresent();
        assertThat(foundTeacher2).isPresent();
        assertThat(foundTeacher1.get().getId()).isNotEqualTo(foundTeacher2.get().getId());
    }

    // ==================== deleteById() Tests ====================

    @Test
    @DisplayName("Should delete teacher by ID successfully")
    void testDeleteById_Success() {
        // Arrange
        Long teacherId = testTeacher.getId();

        // Act
        teacherRepository.deleteById(teacherId);
        entityManager.flush();

        // Assert
        Optional<Teacher> deletedTeacher = teacherRepository.findById(teacherId);
        assertThat(deletedTeacher).isEmpty();
    }

    @Test
    @DisplayName("Should handle deletion of non-existent teacher gracefully")
    void testDeleteById_NonExistent() {
        // Act & Assert - JPA doesn't throw exception for deleting non-existent entity
        teacherRepository.deleteById(99999L);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should not find deleted teacher by email")
    void testDeleteById_EmailNotFound() {
        // Arrange
        Long teacherId = testTeacher.getId();
        String email = testTeacher.getEmail();

        // Act
        teacherRepository.deleteById(teacherId);
        entityManager.flush();

        // Assert
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail(email);
        assertThat(foundTeacher).isEmpty();
    }

    // ==================== Update Tests ====================

    @Test
    @DisplayName("Should update teacher successfully")
    void testUpdate_Success() {
        // Arrange
        Teacher teacherToUpdate = teacherRepository.findById(testTeacher.getId()).orElseThrow();
        teacherToUpdate.setName("Dr. Updated Name");
        teacherToUpdate.setEmail("updated.email@school.com");
        teacherToUpdate.setDepartment("Software Engineering");

        // Act
        Teacher updatedTeacher = teacherRepository.save(teacherToUpdate);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Teacher foundTeacher = teacherRepository.findById(updatedTeacher.getId()).orElseThrow();
        assertThat(foundTeacher.getName()).isEqualTo("Dr. Updated Name");
        assertThat(foundTeacher.getEmail()).isEqualTo("updated.email@school.com");
        assertThat(foundTeacher.getDepartment()).isEqualTo("Software Engineering");
    }

    @Test
    @DisplayName("Should update only specific fields")
    void testUpdate_PartialUpdate() {
        // Arrange
        Teacher teacherToUpdate = teacherRepository.findById(testTeacher.getId()).orElseThrow();
        String originalEmail = teacherToUpdate.getEmail();
        String originalDepartment = teacherToUpdate.getDepartment();
        teacherToUpdate.setName("Dr. Partially Updated");

        // Act
        Teacher updatedTeacher = teacherRepository.save(teacherToUpdate);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Teacher foundTeacher = teacherRepository.findById(updatedTeacher.getId()).orElseThrow();
        assertThat(foundTeacher.getName()).isEqualTo("Dr. Partially Updated");
        assertThat(foundTeacher.getEmail()).isEqualTo(originalEmail);
        assertThat(foundTeacher.getDepartment()).isEqualTo(originalDepartment);
    }

    // ==================== Count Tests ====================

    @Test
    @DisplayName("Should count teachers correctly")
    void testCount() {
        // Arrange
        long initialCount = teacherRepository.count();

        Teacher newTeacher = new Teacher();
        newTeacher.setName("Dr. Count Test");
        newTeacher.setEmail("count.test@school.com");
        newTeacher.setDepartment("Statistics");
        newTeacher.setStudents(new ArrayList<>());
        newTeacher.setCourses(new ArrayList<>());
        teacherRepository.save(newTeacher);
        entityManager.flush();

        // Act
        long newCount = teacherRepository.count();

        // Assert
        assertThat(newCount).isEqualTo(initialCount + 1);
    }

    @Test
    @DisplayName("Should return zero count when no teachers exist")
    void testCount_Zero() {
        // Arrange
        teacherRepository.deleteAll();
        entityManager.flush();

        // Act
        long count = teacherRepository.count();

        // Assert
        assertThat(count).isZero();
    }

    // ==================== Exists Tests ====================

    @Test
    @DisplayName("Should check if teacher exists by ID")
    void testExistsById() {
        // Act
        boolean exists = teacherRepository.existsById(testTeacher.getId());
        boolean notExists = teacherRepository.existsById(99999L);

        // Assert
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Should handle multiple teachers in same department")
    void testMultipleTeachersSameDepartment() {
        // Arrange
        Teacher teacher2 = new Teacher();
        teacher2.setName("Dr. Second CS Teacher");
        teacher2.setEmail("second.cs@school.com");
        teacher2.setDepartment("Computer Science");
        teacher2.setStudents(new ArrayList<>());
        teacher2.setCourses(new ArrayList<>());

        Teacher teacher3 = new Teacher();
        teacher3.setName("Dr. Third CS Teacher");
        teacher3.setEmail("third.cs@school.com");
        teacher3.setDepartment("Computer Science");
        teacher3.setStudents(new ArrayList<>());
        teacher3.setCourses(new ArrayList<>());

        // Act
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);
        entityManager.flush();

        List<Teacher> allTeachers = teacherRepository.findAll();

        // Assert
        assertThat(allTeachers).hasSizeGreaterThanOrEqualTo(3);
        assertThat(allTeachers.stream()
                .filter(t -> t.getDepartment().equals("Computer Science"))
                .count()).isGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("Should maintain data integrity after multiple operations")
    void testDataIntegrity() {
        // Arrange & Act
        Teacher newTeacher = new Teacher();
        newTeacher.setName("Dr. Integrity Test");
        newTeacher.setEmail("integrity@school.com");
        newTeacher.setDepartment("Data Science");
        newTeacher.setStudents(new ArrayList<>());
        newTeacher.setCourses(new ArrayList<>());
        Teacher saved = teacherRepository.save(newTeacher);
        entityManager.flush();

        saved.setName("Dr. Integrity Test Updated");
        Teacher updated = teacherRepository.save(saved);
        entityManager.flush();

        entityManager.clear();

        // Assert
        Teacher found = teacherRepository.findById(updated.getId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("Dr. Integrity Test Updated");
        assertThat(found.getEmail()).isEqualTo("integrity@school.com");
    }

    @Test
    @DisplayName("Should persist all collections")
    void testCollectionsPersistence() {
        // Act
        Teacher foundTeacher = teacherRepository.findById(testTeacher.getId()).orElseThrow();

        // Assert
        assertThat(foundTeacher.getStudents()).isNotNull();
        assertThat(foundTeacher.getCourses()).isNotNull();
    }
}

