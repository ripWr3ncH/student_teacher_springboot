package com.example.sepm_assignment.controller;

import com.example.sepm_assignment.model.Teacher;
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
 * Integration Tests for TeacherController
 * Testing Strategy: Test HTTP endpoints with MockMvc
 * Using H2 in-memory database for integration testing
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("TeacherController Integration Tests")
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        // Clean up database
        teacherRepository.deleteAll();

        // Create test teacher
        testTeacher = new Teacher();
        testTeacher.setName("Dr. Test Teacher");
        testTeacher.setEmail("test.teacher@school.com");
        testTeacher.setDepartment("Computer Science");
        testTeacher.setStudents(new ArrayList<>());
        testTeacher.setCourses(new ArrayList<>());
        testTeacher = teacherRepository.save(testTeacher);
    }

    // ==================== GET /api/teachers Tests ====================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should get all teachers with USER role")
    void getAllTeachers() throws Exception {
        mockMvc.perform(get("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is("Dr. Test Teacher")))
                .andExpect(jsonPath("$[0].email", is("test.teacher@school.com")))
                .andExpect(jsonPath("$[0].department", is("Computer Science")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get all teachers with ADMIN role")
    void testGetAllTeachers_AdminRole() throws Exception {
        mockMvc.perform(get("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("Should return 401 when not authenticated")
    void testGetAllTeachers_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ==================== GET /api/teachers/{id} Tests ====================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should get teacher by ID successfully")
    void getTeacherById() throws Exception {
        mockMvc.perform(get("/api/teachers/{id}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testTeacher.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Dr. Test Teacher")))
                .andExpect(jsonPath("$.email", is("test.teacher@school.com")))
                .andExpect(jsonPath("$.department", is("Computer Science")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return 404 when teacher not found by ID")
    void testGetTeacherById_NotFound() throws Exception {
        mockMvc.perform(get("/api/teachers/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 401 when getting teacher by ID without authentication")
    void testGetTeacherById_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/teachers/{id}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ==================== POST /api/teachers Tests ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should create teacher with ADMIN role successfully")
    void createTeacher() throws Exception {
        Teacher newTeacher = new Teacher();
        newTeacher.setName("Dr. New Teacher");
        newTeacher.setEmail("new.teacher@school.com");
        newTeacher.setDepartment("Mathematics");
        newTeacher.setStudents(new ArrayList<>());
        newTeacher.setCourses(new ArrayList<>());

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeacher)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Dr. New Teacher")))
                .andExpect(jsonPath("$.email", is("new.teacher@school.com")))
                .andExpect(jsonPath("$.department", is("Mathematics")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return 403 when USER role tries to create teacher")
    void testCreateTeacher_Forbidden() throws Exception {
        Teacher newTeacher = new Teacher();
        newTeacher.setName("Dr. New Teacher");
        newTeacher.setEmail("new.teacher@school.com");
        newTeacher.setDepartment("Mathematics");

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeacher)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 401 when creating teacher without authentication")
    void testCreateTeacher_Unauthorized() throws Exception {
        Teacher newTeacher = new Teacher();
        newTeacher.setName("Dr. New Teacher");
        newTeacher.setEmail("new.teacher@school.com");
        newTeacher.setDepartment("Mathematics");

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeacher)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ==================== PUT /api/teachers/{id} Tests ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update teacher with ADMIN role successfully")
    void updateTeacher() throws Exception {
        Teacher updatedTeacher = new Teacher();
        updatedTeacher.setName("Dr. Updated Teacher");
        updatedTeacher.setEmail("updated.teacher@school.com");
        updatedTeacher.setDepartment("Software Engineering");

        mockMvc.perform(put("/api/teachers/{id}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTeacher)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dr. Updated Teacher")))
                .andExpect(jsonPath("$.email", is("updated.teacher@school.com")))
                .andExpect(jsonPath("$.department", is("Software Engineering")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return 403 when USER role tries to update teacher")
    void testUpdateTeacher_Forbidden() throws Exception {
        Teacher updatedTeacher = new Teacher();
        updatedTeacher.setName("Dr. Updated Teacher");
        updatedTeacher.setEmail("updated.teacher@school.com");
        updatedTeacher.setDepartment("Software Engineering");

        mockMvc.perform(put("/api/teachers/{id}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTeacher)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return 404 when updating non-existent teacher")
    void testUpdateTeacher_NotFound() throws Exception {
        Teacher updatedTeacher = new Teacher();
        updatedTeacher.setName("Dr. Updated Teacher");
        updatedTeacher.setEmail("updated.teacher@school.com");
        updatedTeacher.setDepartment("Software Engineering");

        mockMvc.perform(put("/api/teachers/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTeacher)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE /api/teachers/{id} Tests ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should delete teacher with ADMIN role successfully")
    void deleteTeacher() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify teacher is deleted
        mockMvc.perform(get("/api/teachers/{id}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return 403 when USER role tries to delete teacher")
    void testDeleteTeacher_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 401 when deleting teacher without authentication")
    void testDeleteTeacher_Unauthorized() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ==================== Validation Tests ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should handle malformed JSON request")
    void testCreateTeacher_MalformedJson() throws Exception {
        String malformedJson = "{name: 'Invalid JSON'}";

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should handle invalid path variable")
    void testGetTeacherById_InvalidPathVariable() throws Exception {
        mockMvc.perform(get("/api/teachers/{id}", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // ==================== Edge Cases ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should create multiple teachers successfully")
    void testCreateMultipleTeachers() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setName("Dr. Teacher One");
        teacher1.setEmail("teacher.one@school.com");
        teacher1.setDepartment("Physics");
        teacher1.setStudents(new ArrayList<>());
        teacher1.setCourses(new ArrayList<>());

        Teacher teacher2 = new Teacher();
        teacher2.setName("Dr. Teacher Two");
        teacher2.setEmail("teacher.two@school.com");
        teacher2.setDepartment("Chemistry");
        teacher2.setStudents(new ArrayList<>());
        teacher2.setCourses(new ArrayList<>());

        // Create first teacher
        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacher1)))
                .andExpect(status().isCreated());

        // Create second teacher
        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacher2)))
                .andExpect(status().isCreated());

        // Verify both teachers exist
        mockMvc.perform(get("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3)))); // Including the setup teacher
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update teacher partially")
    void testUpdateTeacher_Partial() throws Exception {
        Teacher partialUpdate = new Teacher();
        partialUpdate.setName("Dr. Partially Updated");
        partialUpdate.setEmail(testTeacher.getEmail()); // Keep same email
        partialUpdate.setDepartment(testTeacher.getDepartment()); // Keep same department

        mockMvc.perform(put("/api/teachers/{id}", testTeacher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dr. Partially Updated")));
    }
}

