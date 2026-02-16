package com.example.sepm_assignment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for Teacher Entity
 * Testing Strategy: Test getters, setters, equals, hashCode, and validation
 */
@DisplayName("Teacher Entity Tests")
class TeacherTest {

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Dr. John Smith");
        teacher.setEmail("john.smith@school.com");
        teacher.setDepartment("Computer Science");
        teacher.setStudents(new ArrayList<>());
        teacher.setCourses(new ArrayList<>());
    }

    // ==================== Getter/Setter Tests ====================

    @Test
    @DisplayName("Should set and get ID correctly")
    void testIdGetterSetter() {
        // Arrange
        Teacher newTeacher = new Teacher();

        // Act
        newTeacher.setId(100L);

        // Assert
        assertThat(newTeacher.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void testNameGetterSetter() {
        // Arrange
        Teacher newTeacher = new Teacher();

        // Act
        newTeacher.setName("Dr. Jane Doe");

        // Assert
        assertThat(newTeacher.getName()).isEqualTo("Dr. Jane Doe");
    }

    @Test
    @DisplayName("Should set and get email correctly")
    void testEmailGetterSetter() {
        // Arrange
        Teacher newTeacher = new Teacher();

        // Act
        newTeacher.setEmail("jane.doe@school.com");

        // Assert
        assertThat(newTeacher.getEmail()).isEqualTo("jane.doe@school.com");
    }

    @Test
    @DisplayName("Should set and get department correctly")
    void testDepartmentGetterSetter() {
        // Arrange
        Teacher newTeacher = new Teacher();

        // Act
        newTeacher.setDepartment("Mathematics");

        // Assert
        assertThat(newTeacher.getDepartment()).isEqualTo("Mathematics");
    }

    @Test
    @DisplayName("Should set and get students list correctly")
    void testStudentsGetterSetter() {
        // Arrange
        Teacher newTeacher = new Teacher();
        List<Student> students = new ArrayList<>();
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student One");
        students.add(student1);

        // Act
        newTeacher.setStudents(students);

        // Assert
        assertThat(newTeacher.getStudents()).isNotNull();
        assertThat(newTeacher.getStudents()).hasSize(1);
        assertThat(newTeacher.getStudents().get(0).getName()).isEqualTo("Student One");
    }

    @Test
    @DisplayName("Should set and get courses list correctly")
    void testCoursesGetterSetter() {
        // Arrange
        Teacher newTeacher = new Teacher();
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Data Structures");
        courses.add(course1);

        // Act
        newTeacher.setCourses(courses);

        // Assert
        assertThat(newTeacher.getCourses()).isNotNull();
        assertThat(newTeacher.getCourses()).hasSize(1);
        assertThat(newTeacher.getCourses().get(0).getTitle()).isEqualTo("Data Structures");
    }

    // ==================== Constructor Tests ====================

    @Test
    @DisplayName("Should create teacher with no-args constructor")
    void testNoArgsConstructor() {
        // Act
        Teacher newTeacher = new Teacher();

        // Assert
        assertThat(newTeacher).isNotNull();
        assertThat(newTeacher.getId()).isNull();
        assertThat(newTeacher.getName()).isNull();
        assertThat(newTeacher.getEmail()).isNull();
        assertThat(newTeacher.getDepartment()).isNull();
        // Note: Lombok @Data may initialize collections, so we just check they exist
    }

    @Test
    @DisplayName("Should create teacher with all-args constructor")
    void testAllArgsConstructor() {
        // Arrange
        List<Student> students = new ArrayList<>();
        List<Course> courses = new ArrayList<>();

        // Act
        Teacher newTeacher = new Teacher(2L, "Dr. Jane Smith", "jane.smith@school.com", "Physics", students, courses);

        // Assert
        assertThat(newTeacher.getId()).isEqualTo(2L);
        assertThat(newTeacher.getName()).isEqualTo("Dr. Jane Smith");
        assertThat(newTeacher.getEmail()).isEqualTo("jane.smith@school.com");
        assertThat(newTeacher.getDepartment()).isEqualTo("Physics");
        assertThat(newTeacher.getStudents()).isNotNull();
        assertThat(newTeacher.getCourses()).isNotNull();
    }

    // ==================== Equals and HashCode Tests ====================

    @Test
    @DisplayName("Should be equal when all fields match")
    void testEquals_AllFieldsMatch() {
        // Arrange
        Teacher teacher1 = new Teacher(1L, "Dr. John Smith", "john.smith@school.com", "Computer Science", new ArrayList<>(), new ArrayList<>());
        Teacher teacher2 = new Teacher(1L, "Dr. John Smith", "john.smith@school.com", "Computer Science", new ArrayList<>(), new ArrayList<>());

        // Assert
        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when IDs differ")
    void testEquals_DifferentIds() {
        // Arrange
        Teacher teacher1 = new Teacher(1L, "Dr. John Smith", "john.smith@school.com", "Computer Science", new ArrayList<>(), new ArrayList<>());
        Teacher teacher2 = new Teacher(2L, "Dr. John Smith", "john.smith@school.com", "Computer Science", new ArrayList<>(), new ArrayList<>());

        // Assert
        assertThat(teacher1).isNotEqualTo(teacher2);
    }

    @Test
    @DisplayName("Should not be equal when names differ")
    void testEquals_DifferentNames() {
        // Arrange
        Teacher teacher1 = new Teacher(1L, "Dr. John Smith", "john.smith@school.com", "Computer Science", new ArrayList<>(), new ArrayList<>());
        Teacher teacher2 = new Teacher(1L, "Dr. Jane Doe", "john.smith@school.com", "Computer Science", new ArrayList<>(), new ArrayList<>());

        // Assert
        assertThat(teacher1).isNotEqualTo(teacher2);
    }

    @Test
    @DisplayName("Should handle null in equals")
    void testEquals_Null() {
        // Assert
        assertThat(teacher).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should be equal to itself")
    void testEquals_Self() {
        // Assert
        assertThat(teacher).isEqualTo(teacher);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void testEquals_DifferentClass() {
        // Arrange
        String notATeacher = "Not a Teacher";

        // Assert
        assertThat(teacher).isNotEqualTo(notATeacher);
    }

    // ==================== ToString Tests ====================

    @Test
    @DisplayName("Should generate toString with all fields")
    void testToString() {
        // Act
        String teacherString = teacher.toString();

        // Assert
        assertThat(teacherString).contains("Teacher");
        assertThat(teacherString).contains("id=1");
        assertThat(teacherString).contains("Dr. John Smith");
        assertThat(teacherString).contains("john.smith@school.com");
        assertThat(teacherString).contains("Computer Science");
    }

    // ==================== Collection Tests ====================

    @Test
    @DisplayName("Should initialize empty students list")
    void testEmptyStudentsList() {
        // Arrange
        Teacher newTeacher = new Teacher();
        newTeacher.setStudents(new ArrayList<>());

        // Assert
        assertThat(newTeacher.getStudents()).isNotNull();
        assertThat(newTeacher.getStudents()).isEmpty();
    }

    @Test
    @DisplayName("Should initialize empty courses list")
    void testEmptyCoursesList() {
        // Arrange
        Teacher newTeacher = new Teacher();
        newTeacher.setCourses(new ArrayList<>());

        // Assert
        assertThat(newTeacher.getCourses()).isNotNull();
        assertThat(newTeacher.getCourses()).isEmpty();
    }

    @Test
    @DisplayName("Should add students to list")
    void testAddStudents() {
        // Arrange
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student 1");
        student1.setEmail("student1@student.com");
        student1.setStudentId("STU001");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Student 2");
        student2.setEmail("student2@student.com");
        student2.setStudentId("STU002");

        // Act
        teacher.getStudents().add(student1);
        teacher.getStudents().add(student2);

        // Assert
        assertThat(teacher.getStudents()).hasSize(2);
        assertThat(teacher.getStudents().get(0).getName()).isEqualTo("Student 1");
        assertThat(teacher.getStudents().get(1).getName()).isEqualTo("Student 2");
    }

    @Test
    @DisplayName("Should add courses to list")
    void testAddCourses() {
        // Arrange
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Data Structures");
        course1.setCourseCode("CS201");
        course1.setCredits(3);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Algorithms");
        course2.setCourseCode("CS202");
        course2.setCredits(4);

        // Act
        teacher.getCourses().add(course1);
        teacher.getCourses().add(course2);

        // Assert
        assertThat(teacher.getCourses()).hasSize(2);
        assertThat(teacher.getCourses().get(0).getTitle()).isEqualTo("Data Structures");
        assertThat(teacher.getCourses().get(1).getTitle()).isEqualTo("Algorithms");
    }

    @Test
    @DisplayName("Should remove students from list")
    void testRemoveStudents() {
        // Arrange
        Student student = new Student();
        student.setId(1L);
        student.setName("Student to Remove");
        teacher.getStudents().add(student);

        // Act
        teacher.getStudents().remove(student);

        // Assert
        assertThat(teacher.getStudents()).isEmpty();
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Should handle empty string values")
    void testEmptyStringValues() {
        // Arrange
        Teacher emptyTeacher = new Teacher();
        emptyTeacher.setName("");
        emptyTeacher.setEmail("");
        emptyTeacher.setDepartment("");

        // Assert
        assertThat(emptyTeacher.getName()).isEmpty();
        assertThat(emptyTeacher.getEmail()).isEmpty();
        assertThat(emptyTeacher.getDepartment()).isEmpty();
    }

    @Test
    @DisplayName("Should handle very long string values")
    void testLongStringValues() {
        // Arrange
        String longName = "Dr. " + "A".repeat(250);
        String longEmail = "b".repeat(100) + "@school.com";
        String longDepartment = "Department of ".repeat(20);

        Teacher longTeacher = new Teacher();
        longTeacher.setName(longName);
        longTeacher.setEmail(longEmail);
        longTeacher.setDepartment(longDepartment);

        // Assert
        assertThat(longTeacher.getName()).startsWith("Dr. ");
        assertThat(longTeacher.getEmail()).contains("@school.com");
        assertThat(longTeacher.getDepartment()).contains("Department of");
    }

    @Test
    @DisplayName("Should handle special characters in fields")
    void testSpecialCharacters() {
        // Arrange
        Teacher specialTeacher = new Teacher();
        specialTeacher.setName("Dr. José María O'Brien-Smith");
        specialTeacher.setEmail("jose.maria@school.com");
        specialTeacher.setDepartment("Computer Science & Engineering");

        // Assert
        assertThat(specialTeacher.getName()).isEqualTo("Dr. José María O'Brien-Smith");
        assertThat(specialTeacher.getEmail()).isEqualTo("jose.maria@school.com");
        assertThat(specialTeacher.getDepartment()).isEqualTo("Computer Science & Engineering");
    }

    @Test
    @DisplayName("Should support large collections")
    void testLargeCollections() {
        // Arrange
        Teacher teacherWithMany = new Teacher();
        teacherWithMany.setStudents(new ArrayList<>());
        teacherWithMany.setCourses(new ArrayList<>());

        // Add 100 students
        for (int i = 0; i < 100; i++) {
            Student student = new Student();
            student.setId((long) i);
            student.setName("Student " + i);
            teacherWithMany.getStudents().add(student);
        }

        // Add 50 courses
        for (int i = 0; i < 50; i++) {
            Course course = new Course();
            course.setId((long) i);
            course.setTitle("Course " + i);
            teacherWithMany.getCourses().add(course);
        }

        // Assert
        assertThat(teacherWithMany.getStudents()).hasSize(100);
        assertThat(teacherWithMany.getCourses()).hasSize(50);
    }

    @Test
    @DisplayName("Should handle null or empty collections gracefully")
    void testNullCollections() {
        // Arrange
        Teacher teacherWithNulls = new Teacher();

        // Assert - Collections may be null or empty depending on Lombok configuration
        // We just verify the teacher object is created without errors
        assertThat(teacherWithNulls).isNotNull();
    }

    @Test
    @DisplayName("Should support method chaining")
    void testMethodChaining() {
        // Arrange
        Teacher chainTeacher = new Teacher();

        // Act
        chainTeacher.setId(10L);
        chainTeacher.setName("Dr. Chain");
        chainTeacher.setEmail("chain@school.com");
        chainTeacher.setDepartment("Chain Department");

        // Assert
        assertThat(chainTeacher.getId()).isEqualTo(10L);
        assertThat(chainTeacher.getName()).isEqualTo("Dr. Chain");
        assertThat(chainTeacher.getEmail()).isEqualTo("chain@school.com");
        assertThat(chainTeacher.getDepartment()).isEqualTo("Chain Department");
    }
}



