package com.lsc.todo_app.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lsc.todo_app.api.dto.task.CreateTaskRequest;
import com.lsc.todo_app.api.dto.task.TaskDTO;
import com.lsc.todo_app.api.dto.task.TaskPageDTO;
import com.lsc.todo_app.api.dto.task.TaskSumarryDTO;
import com.lsc.todo_app.api.dto.task.UpdateTaskRequest;
import com.lsc.todo_app.api.dto.user.UserSummaryDTO;
import com.lsc.todo_app.domain.entity.Task;
import com.lsc.todo_app.domain.entity.User;
import com.lsc.todo_app.domain.entity.enums.Status;
import com.lsc.todo_app.domain.repository.TaskRepository;
import com.lsc.todo_app.domain.repository.UserRepository;

@Service
public class TaskService {
    
    @Autowired
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDTO createTask(CreateTaskRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found with id " + request.getUserId()));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(Status.PENDING);
        task.setUser(user);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        task = taskRepository.save(task);
        user.getTasks().add(task);

        return new TaskDTO(task, new UserSummaryDTO(user));
    }

    public TaskPageDTO readTasks(Pageable pageable) {
        Page<Task> taskPage = taskRepository.findAll(pageable);

        List<TaskSumarryDTO> taskSumarryDTOs = taskPage.getContent().stream()
        .map(task -> {
            return new TaskSumarryDTO(task);
        }).collect(Collectors.toList());

        return new TaskPageDTO(taskSumarryDTOs, taskPage.getNumber(), taskPage.getTotalPages(), taskPage.getTotalElements(), taskPage.getSize());
    }

    public TaskDTO readTaskById(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id " + id));

        return new TaskDTO(task, new UserSummaryDTO(task.getUser()));
    }

    public TaskDTO updateTask(Long id, UpdateTaskRequest requestTask) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id " + id));
            if (requestTask.getTitle() != null){
                task.setTitle(requestTask.getTitle());
            }
            if (requestTask.getDescription() != null){
                task.setDescription(requestTask.getDescription());
            }
            if (requestTask.getStatus() != null){
                task.setStatus(requestTask.getStatus());
            }
            task.setUpdatedAt(LocalDateTime.now());

            Task taskUpdated = taskRepository.save(task);
            return new TaskDTO(taskUpdated, new UserSummaryDTO(taskUpdated.getUser()));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)){
            throw new RuntimeException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
    }
}
