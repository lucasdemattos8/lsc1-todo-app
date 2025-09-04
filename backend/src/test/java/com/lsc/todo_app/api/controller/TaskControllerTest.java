package com.lsc.todo_app.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.lsc.todo_app.api.dto.task.CreateTaskRequest;
import com.lsc.todo_app.api.dto.task.TaskDTO;
import com.lsc.todo_app.api.dto.task.TaskPageDTO;
import com.lsc.todo_app.api.dto.task.TaskSummaryDTO;
import com.lsc.todo_app.api.dto.task.UpdateTaskRequest;
import com.lsc.todo_app.api.util.URIUtil;
import com.lsc.todo_app.domain.service.TaskService;
import com.lsc.todo_app.utils.dto.TaskDTODataFactory;

@DisplayName("TaskController - CRUD Unit Tests")
public class TaskControllerTest {
    
    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @Mock
    private URIUtil uriUtil;

    private TaskDTODataFactory taskDTODataFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        taskDTODataFactory = new TaskDTODataFactory();
    }

    @DisplayName("Should create a task and return 201 status with non-null TaskDTO body")
    @Test
    void shouldCreateCorrectlyTask() {
        final URI expectedURI = URI.create("http://localhost/tasks/1");
        CreateTaskRequest request = taskDTODataFactory.createCreateTaskRequest();
        TaskDTO taskDTO = taskDTODataFactory.createTaskDTO();

        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(taskDTO);
        when(uriUtil.createNewURIById(1L)).thenReturn(expectedURI);

        ResponseEntity<TaskDTO> response = taskController.createTask(request);

        assertNotNull(response, "Expected the response is not null");
        assertEquals(201, response.getStatusCode().value(), "Expected HTTP Status 201 for task creation");

        assertNotNull(response.getBody(), "Expected the response body is not null");
        
        verify(taskService, times(1)).createTask(any(CreateTaskRequest.class));
    }

    @DisplayName("Should read pageable tasks and return 200 status with non-null TaskPageDTO body")
    @Test
    void shouldReadCorrectlyTasks() {
        TaskSummaryDTO taskSumarry = taskDTODataFactory.createTaskSummaryDTO();

        List<TaskSummaryDTO> tasksList = Arrays.asList(taskSumarry);

        TaskPageDTO contentPage = new TaskPageDTO(tasksList, 0, 1, 1, 20);

        when(taskService.readTasks(any(Pageable.class))).thenReturn(contentPage);

        ResponseEntity<TaskPageDTO> response = taskController.readTasks(Pageable.unpaged());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        verify(taskService, times(1)).readTasks(Pageable.unpaged());
    }

    @DisplayName("Should read a task by ID and return 200 status with non-null TaskDTO body")
    @Test
    void shouldReadCorrectlyTask() {
        TaskDTO taskDTO = taskDTODataFactory.createTaskDTO();

        when(taskService.readTaskById(anyLong())).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = taskController.readTasksById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        verify(taskService, times(1)).readTaskById(anyLong());
    }

    @DisplayName("Should update a task by ID and return 200 status with non-null TaskDTO body")
    @Test
    void shouldUpdateCorrectlyTask() {
        TaskDTO testTask = taskDTODataFactory.createTaskDTO();
        UpdateTaskRequest updateRequest = taskDTODataFactory.createUpdateTaskRequest();

        when(taskService.updateTask(anyLong(), any(UpdateTaskRequest.class))).thenReturn(testTask);

        ResponseEntity<TaskDTO> response = taskController.updateTask(1L, updateRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        verify(taskService, times(1)).updateTask(anyLong(), any(UpdateTaskRequest.class));
    }

    @DisplayName("Should delete a task by ID and return 204 status with null body")
    @Test
    void shouldDeleteCorrectlyTask() {
        TaskDTO testTask = taskDTODataFactory.createTaskDTO();

        doNothing().when(taskService).deleteTask(anyLong());

        ResponseEntity<?> response = taskController.deleteTask(testTask.getId());

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());

        assertNull(response.getBody());

        verify(taskService, times(1)).deleteTask(anyLong());
    }
}
