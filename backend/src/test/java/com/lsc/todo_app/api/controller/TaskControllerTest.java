package com.lsc.todo_app.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

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
import com.lsc.todo_app.api.dto.task.TaskDTO;
import com.lsc.todo_app.api.dto.task.UpdateTaskRequest;
import com.lsc.todo_app.api.dto.user.UserDTO;
import com.lsc.todo_app.api.dto.user.UserRequest;
import com.lsc.todo_app.domain.entity.enums.Status;
import com.lsc.todo_app.domain.service.TaskService;
import com.lsc.todo_app.domain.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    private UserDTO testUser;

    private TaskDTO testTask;

    @BeforeEach
    void setUp() {
        UserRequest newUser = new UserRequest("João Silva", "joão@gmail.com", "123456");
        testUser = userService.createUser(newUser);    

        CreateTaskRequest newTask = new CreateTaskRequest("Learn HTTP", "Understand the basics of Http", testUser.getId());
        testTask = taskService.createTask(newTask);
    }

    @Test
    void shouldCreateTask() throws Exception {
        CreateTaskRequest newTask = new CreateTaskRequest();
        newTask.setTitle("Learn Spring");
        newTask.setDescription("Understand the basics of Spring Boot");
        newTask.setUserId(testUser.getId());

        mockMvc.perform(post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Learn Spring")))
                .andExpect(jsonPath("$.description", is("Understand the basics of Spring Boot")))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.id", is(testUser.getId().intValue())));
    }

    @Test
    void shouldReadPagedTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks").exists())
                .andExpect(jsonPath("$.tasks").isArray())
                .andExpect(jsonPath("$.tasks[0].title", is("Learn HTTP")))
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.pageSize", is(20)));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        UpdateTaskRequest updatedTask = new UpdateTaskRequest(
            "Learn Spring", "Understand the basics of Spring Boot", Status.COMPLETED);

        String createdAtFormatted = testTask.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        mockMvc.perform(put("/api/tasks/" + testTask.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Learn Spring")))
                .andExpect(jsonPath("$.description", is("Understand the basics of Spring Boot")))
                .andExpect(jsonPath("$.status", is("COMPLETED")))
                .andExpect(jsonPath("$.createdAt", matchesPattern(createdAtFormatted + ".*")))
                .andExpect(jsonPath("$.updatedAt", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?")))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.id", is(testUser.getId().intValue())));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()))
                .andExpect(status().isNoContent());
    }
}
