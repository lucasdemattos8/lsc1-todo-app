package com.lsc.todo_app.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.lsc.todo_app.api.dto.task.CreateTaskRequest;
import com.lsc.todo_app.api.dto.task.TaskDTO;
import com.lsc.todo_app.api.dto.task.TaskPageDTO;
import com.lsc.todo_app.api.dto.task.TaskSumarryDTO;
import com.lsc.todo_app.api.dto.task.UpdateTaskRequest;
import com.lsc.todo_app.api.dto.user.UserSumarryDTO;
import com.lsc.todo_app.domain.entity.Task;
import com.lsc.todo_app.domain.entity.User;
import com.lsc.todo_app.domain.entity.enums.Status;
import com.lsc.todo_app.domain.repository.TaskRepository;
import com.lsc.todo_app.domain.repository.UserRepository;
import com.lsc.todo_app.utils.domain.TaskDomainDataFactory;
import com.lsc.todo_app.utils.dto.TaskDTODataFactory;

@DisplayName("TaskService - CRUD Unit Tests")
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private TaskDTODataFactory taskDTODataFactory;
    private TaskDomainDataFactory taskDomainDataFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        taskDTODataFactory = new TaskDTODataFactory();
        taskDomainDataFactory = new TaskDomainDataFactory();
    }

    @Test
    @DisplayName("Should correctly create a Task and call save in repository")
    void shouldCreateCorrectlyTask() {
        CreateTaskRequest createTaskRequest = taskDTODataFactory.createCreateTaskRequest();
        Task task = taskDomainDataFactory.createTaskWithUser();
        final User user = task.getUser();
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO createdTaskDTO = taskService.createTask(createTaskRequest);

        final UserSumarryDTO taskOwner = createdTaskDTO.getUser();

        assertNotNull(createdTaskDTO.getUpdatedAt());
        assertNotNull(createdTaskDTO.getCreatedAt());
        assertNotNull(createdTaskDTO.getId());
        assertEquals(createTaskRequest.getTitle(), createdTaskDTO.getTitle());
        assertEquals(createTaskRequest.getDescription(), createdTaskDTO.getDescription());
        assertEquals(createTaskRequest.getUserId(), taskOwner.getId());
        assertEquals(Status.PENDING, createdTaskDTO.getStatus());
    }

    @Test
    @DisplayName("Should not create a Task and throws an Exception") 
    void shouldThrowsExceptionWhenEntityNotFoundInDatabase() {
        CreateTaskRequest createTaskRequest = taskDTODataFactory.createCreateTaskRequest();
        Task task = taskDomainDataFactory.createTaskWithUser();
        final User user = task.getUser();

        createTaskRequest.setUserId(99L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Exception responseException = assertThrows(RuntimeException.class, () -> taskService.createTask(createTaskRequest));
        final String expectedMessage = "User not found with id " + createTaskRequest.getUserId();

        assertEquals(responseException.getMessage(), expectedMessage);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return a pageable data by findAll from repository")
    void shouldReturnAPagebleDataFromDatabase() {
        Task firstTask = taskDomainDataFactory.createTaskWithUser();
        Task secondTask = taskDomainDataFactory.createTaskWithUser();
        Task thirdTask = taskDomainDataFactory.createTaskWithUser();

        firstTask.setId(2L);
        secondTask.setId(3L);
        thirdTask.setId(4L);

        final List<Task> taskList = List.of(firstTask, secondTask, thirdTask);
        final Page<Task> pageableTasks = new PageImpl<>(taskList);
        final Pageable pageable = PageRequest.of(
            0, 10, Sort.by("id").descending());
        
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(pageableTasks);

        final TaskPageDTO response = taskService.readTasks(pageable);

        final List<TaskSumarryDTO> responseContent = response.getTasks();
        final int expectedElements = 3;

        assertNotNull(response);
        assertNotNull(responseContent);
        assertEquals(expectedElements, response.getTotalElements());
        assertEquals(expectedElements, responseContent.size());
    }

    @Test
    @DisplayName("Should return correctly a TaskDTO data by ID from repository")
    void shouldReturnCorrectlyATaskDTOFromRepository() {
        final Task task = taskDomainDataFactory.createTaskWithUser();
        final User taskOwner = task.getUser();
        final Long idToFind = 1L;

        when(taskRepository.findById(idToFind)).thenReturn(
            Optional.of(task));

        final TaskDTO response = taskService.readTaskById(idToFind);
        final UserSumarryDTO taskOwnerResponse = response.getUser();

        assertEquals(idToFind, response.getId());
        assertEquals(task.getTitle(), response.getTitle());
        assertEquals(task.getDescription(), response.getDescription());
        assertEquals(task.getStatus(), response.getStatus());
        assertEquals(task.getCreatedAt(), response.getCreatedAt());
        assertEquals(task.getUpdatedAt(), response.getUpdatedAt());
        assertEquals(taskOwner.getId(), taskOwnerResponse.getId());
    }

    @Test
    @DisplayName("Should throws an Exception when Task doesnt exists by the provided ID from repository when ReadTask")
    void shouldThrowsExceptionWhenInFindTaskDoesntExistsTheProvidedID() {
        final Task task = taskDomainDataFactory.createTaskWithUser();
        final Long idToFind = 99L;

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        final Exception exceptionResponse = assertThrows(
                RuntimeException.class, () -> taskService.readTaskById(idToFind));
        
        final String expectedMessage = "Task not found with id " + idToFind;

        assertEquals(expectedMessage, exceptionResponse.getMessage());
    }

    @Test
    @DisplayName("Should update a Task correctly when founded by ID from repository")
    void shouldUpdateTaskWhenCorrectlyFound() {
        Task task = taskDomainDataFactory.createTaskWithUser();
        task.setUpdatedAt(LocalDateTime.now().minusHours(1L));

        final UpdateTaskRequest updateRequestDTO = new UpdateTaskRequest("Teste", "Estou sendo testado", Status.COMPLETED);
        final Long idToUpdate = 1L;
        final LocalDateTime beforeUpdateTask = task.getUpdatedAt();

        when(taskRepository.findById(idToUpdate)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final TaskDTO response = taskService.updateTask(idToUpdate, updateRequestDTO);

        final LocalDateTime afterUpdateTask = response.getUpdatedAt();

        assertEquals(idToUpdate, response.getId());
        assertEquals(updateRequestDTO.getTitle(), response.getTitle());
        assertEquals(updateRequestDTO.getDescription(), response.getDescription());
        assertEquals(updateRequestDTO.getStatus(), response.getStatus());
        assertEquals(task.getCreatedAt(), response.getCreatedAt());
        assertTrue(beforeUpdateTask.isBefore(afterUpdateTask));
        
    }

    @Test
    @DisplayName("Should throws an Exception when Task doesnt exists by the provided ID from repository when Update Task")
    void shouldThrowsExceptionWhenInUpdateTaskDoesntExistsTheProvidedID() {
        final Task task = taskDomainDataFactory.createTaskWithUser();
        final UpdateTaskRequest updateRequestDTO = new UpdateTaskRequest("Teste", "Estou sendo testado", Status.COMPLETED);
        final Long idToUpdate = 99L;

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        final Exception exceptionResponse = assertThrows(
                RuntimeException.class, () -> taskService.updateTask(idToUpdate, updateRequestDTO));
        
        final String expectedMessage = "Task not found with id " + idToUpdate;

        assertEquals(expectedMessage, exceptionResponse.getMessage());
    }

    @Test
    @DisplayName("Should delete a Task when the ID exists")
    void shouldDeleteCorrectlyTaskWhenTheIDExists() {
        when(taskRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(taskRepository).deleteById(anyLong());

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should throws an Exception when Task doesnt exists by the provided ID from repository when an Operation to Delete Task")
    void shouldThrowsExceptionWhenInDeleteTaskDoesntExistsTheProvidedID() {
        final Long idToDelete = 99L;
        
        when(taskRepository.existsById(idToDelete)).thenReturn(false);
        
        final Exception exceptionResponse = assertThrows(
                RuntimeException.class, () -> taskService.deleteTask(idToDelete));
        
        final String expectedMessage = "Task not found with id " + idToDelete;
        
        assertEquals(expectedMessage, exceptionResponse.getMessage());
        verify(taskRepository, never()).deleteById(anyLong());
    }
    
}
