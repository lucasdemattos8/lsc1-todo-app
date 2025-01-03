package com.lsc.todo_app.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsc.todo_app.api.dto.task.CreateTaskRequest;
import com.lsc.todo_app.api.dto.user.UserDTO;
import com.lsc.todo_app.api.dto.user.UserRequest;
import com.lsc.todo_app.domain.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private UserDTO testUser;

    @BeforeEach
    void setUp() {
        UserRequest newUser = new UserRequest("João Silva", "joão@gmail.com", "123456");
        testUser = userService.createUser(newUser);
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserRequest newUser = new UserRequest("Paulo Silva", "paulo@gmail.com", "654321");
        
        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(newUser.getName()))
            .andExpect(jsonPath("$.email").value(newUser.getEmail()))
            .andExpect(jsonPath("$.tasks").isEmpty());
    }

    @Test
    void shouldReadPagedUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.users").exists())
            .andExpect(jsonPath("$.users").isArray())
            .andExpect(jsonPath("$.users[0].name", is("João Silva")))
            .andExpect(jsonPath("$.currentPage", is(0)))
            .andExpect(jsonPath("$.totalPages", is(1)))
            .andExpect(jsonPath("$.totalElements", is(1)))
            .andExpect(jsonPath("$.pageSize", is(20)));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserRequest updatedUser = new UserRequest("Paulo Silva", "paulo@gmail.com", "654321");

        mockMvc.perform(put("/api/users/" + testUser.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Paulo Silva")))
                .andExpect(jsonPath("$.email", is("paulo@gmail.com")))
                .andExpect(jsonPath("$.tasks", is(testUser.getTasks())));
    }

    @Test
    void shouldUpdateUserThatHaveOneTask() throws Exception {
        UserRequest updatedUser = new UserRequest("Paulo Silva", "paulo@gmail.com", "654321");
        CreateTaskRequest createTaskRequest = new CreateTaskRequest("Task 1", "Description 1", testUser.getId());

        mockMvc.perform(post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createTaskRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/users/" + testUser.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Paulo Silva")))
                .andExpect(jsonPath("$.email", is("paulo@gmail.com")))
                .andExpect(jsonPath("$.tasks").exists())
                .andExpect(jsonPath("$.tasks[0].title", is("Task 1")))
                .andExpect(jsonPath("$.tasks[0].description", is("Description 1")));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId()))
            .andExpect(status().isNoContent());
    }
}
