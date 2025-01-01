package com.lsc.todo_app.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lsc.todo_app.domain.entity.Task;
import com.lsc.todo_app.domain.repository.TaskRepository;

@Service
public class TaskService {
    
    @Autowired
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Page<Task> readTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Task updateTask(Long id, Task requestTask) {
        return taskRepository.findById(id).map(task -> {
            if (requestTask.getTitle() != null){
                task.setTitle(requestTask.getTitle());
            }
            if (requestTask.getDescription() != null){
                task.setDescription(requestTask.getDescription());
            }
            if (requestTask.getStatus() != null){
                task.setStatus(requestTask.getStatus());
            }
            if (requestTask.getUpdatedAt() != null){
                task.setUpdatedAt(requestTask.getUpdatedAt());
            }
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}
