package com.example.sepm_assignment.controller;

import com.example.sepm_assignment.model.Student;
import com.example.sepm_assignment.model.Teacher;
import com.example.sepm_assignment.repository.StudentRepository;
import com.example.sepm_assignment.repository.TeacherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for StudentController
 * Testing Strategy: Test HTTP endpoints with MockMvc
 * Using H2 in-memory database for integration testing
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("StudentController Integration Tests")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Teacher testTeacher;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        // Clean up database
        studentRepository.deleteAll();
        teacherRepository.deleteAll();

        // Create test teacher
        testTeacher = new Teacher();
        testTeacher.setName("Dr. Integration Test");
        testTeacher.setEmail("integration.test@school.com");
        testTeacher.setDepartment("Computer Science");
        testTeacher.setStudents(new ArrayList<>());
        testTeacher.setCourses(new ArrayList<>());
        testTeacher = teacherRepository.save(testTeacher);

        // Create test student
        testStudent = new Student();
        testStudent.setName("Test Student");
        testStudent.setEmail("test.student@student.com");
        testStudent.setStudentId("STU-TEST-001");
        testStudent.setTeacher(testTeacher);
        testStudent = studentRepository.save(testStudent);
    }

    // ==================== GET /api/students Tests ====================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should get all students with USER role")
    void testGetAllStudents_Success() throws Exception {
        mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is("Test Student")))
                .andExpect(jsonPath("$[0].email", is("test.student@student.com")))
                .andExpect(jsonPath("$[0].studentId", is("STU-TEST-001")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get all students with ADMIN role")
    void testGetAllStudents_AdminRole() throws Exception {
        mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("Should return 401 when not authenticated")
    void testGetAllStudents_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ==================== GET /api/students/{id} Tests ====================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should get student by ID successfully")
    void testGetStudentById_Success() throws Exception {
        mockMvc.perform(get("/api/students/{id}", testStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testStudent.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Student")))
                .andExpect(jsonPath("$.email", is("test.student@student.com")))
                .andExpect(jsonPath("$.studentId", is("STU-TEST-001")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return 404 when student not found by ID")
    void testGetStudentById_NotFound() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 401 when getting student by ID without authentication")
    void testGetStudentById_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/students/{id}", testStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ==================== GET /api/students/teacher/{teacherId} Tests ====================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should get students by teacher ID successfully")
    void testGetStudentsByTeacher_Success() throws Exception {
        mockMvc.perform(get("/api/students/teacher/{teacherId}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is("Test Student")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return empty list when teacher has no students")
    void testGetStudentsByTeacher_NoStudents() throws Exception {
        // Create a teacher with no students
        Teacher emptyTeacher = new Teacher();
        emptyTeacher.setName("Empty Teacher");
        emptyTeacher.setEmail("empty@school.com");
        emptyTeacher.setDepartment("Mathematics");
        emptyTeacher.setStudents(new ArrayList<>());
        emptyTeacher.setCourses(new ArrayList<>());
        emptyTeacher = teacherRepository.save(emptyTeacher);

        mockMvc.perform(get("/api/students/teacher/{teacherId}", emptyTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ==================== POST /api/students/teacher/{teacherId} Tests ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should create student with ADMIN role successfully")
    void testCreateStudent_Success() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("New Student");
        newStudent.setEmail("new.student@student.com");
        newStudent.setStudentId("STU-NEW-001");

        mockMvc.perform(post("/api/students/teacher/{teacherId}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Student")))
                .andExpect(jsonPath("$.email", is("new.student@student.com")))
                .andExpect(jsonPath("$.studentId", is("STU-NEW-001")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return 403 when USER role tries to create student")
    void testCreateStudent_Forbidden() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("New Student");
        newStudent.setEmail("new.student@student.com");
        newStudent.setStudentId("STU-NEW-001");

        mockMvc.perform(post("/api/students/teacher/{teacherId}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return 400 when teacher not found for create student")
    void testCreateStudent_TeacherNotFound() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("New Student");
        newStudent.setEmail("new.student@student.com");
        newStudent.setStudentId("STU-NEW-001");

        mockMvc.perform(post("/api/students/teacher/{teacherId}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 401 when creating student without authentication")
    void testCreateStudent_Unauthorized() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("New Student");
        newStudent.setEmail("new.student@student.com");
        newStudent.setStudentId("STU-NEW-001");

        mockMvc.perform(post("/api/students/teacher/{teacherId}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ==================== PUT /api/students/{id} Tests ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update student with ADMIN role successfully")
    void testUpdateStudent_Success() throws Exception {
        Student updatedStudent = new Student();
        updatedStudent.setName("Updated Student Name");
        updatedStudent.setEmail("updated.student@student.com");
        updatedStudent.setStudentId("STU-UPDATED-001");

        mockMvc.perform(put("/api/students/{id}", testStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Student Name")))
                .andExpect(jsonPath("$.email", is("updated.student@student.com")))
                .andExpect(jsonPath("$.studentId", is("STU-UPDATED-001")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return 403 when USER role tries to update student")
    void testUpdateStudent_Forbidden() throws Exception {
        Student updatedStudent = new Student();
        updatedStudent.setName("Updated Student Name");
        updatedStudent.setEmail("updated.student@student.com");
        updatedStudent.setStudentId("STU-UPDATED-001");

        mockMvc.perform(put("/api/students/{id}", testStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return 404 when updating non-existent student")
    void testUpdateStudent_NotFound() throws Exception {
        Student updatedStudent = new Student();
        updatedStudent.setName("Updated Student Name");
        updatedStudent.setEmail("updated.student@student.com");
        updatedStudent.setStudentId("STU-UPDATED-001");

        mockMvc.perform(put("/api/students/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE /api/students/{id} Tests ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should delete student with ADMIN role successfully")
    void testDeleteStudent_Success() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", testStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify student is deleted
        mockMvc.perform(get("/api/students/{id}", testStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return 403 when USER role tries to delete student")
    void testDeleteStudent_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", testStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 401 when deleting student without authentication")
    void testDeleteStudent_Unauthorized() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", testStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ==================== Validation Tests ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should handle malformed JSON request")
    void testCreateStudent_MalformedJson() throws Exception {
        String malformedJson = "{name: 'Invalid JSON'}";

        mockMvc.perform(post("/api/students/teacher/{teacherId}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should handle invalid path variable")
    void testGetStudentById_InvalidPathVariable() throws Exception {
        mockMvc.perform(get("/api/students/{id}", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // ==================== Edge Cases ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should create multiple students for same teacher")
    void testCreateMultipleStudents_SameTeacher() throws Exception {
        Student student1 = new Student();
        student1.setName("Student One");
        student1.setEmail("student.one@student.com");
        student1.setStudentId("STU-001");

        Student student2 = new Student();
        student2.setName("Student Two");
        student2.setEmail("student.two@student.com");
        student2.setStudentId("STU-002");

        // Create first student
        mockMvc.perform(post("/api/students/teacher/{teacherId}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student1)))
                .andExpect(status().isCreated());

        // Create second student
        mockMvc.perform(post("/api/students/teacher/{teacherId}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student2)))
                .andExpect(status().isCreated());

        // Verify both students exist
        mockMvc.perform(get("/api/students/teacher/{teacherId}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3)))); // Including the setup student
    }
}

