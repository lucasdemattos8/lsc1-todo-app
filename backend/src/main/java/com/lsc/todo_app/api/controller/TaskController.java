package com.lsc.todo_app.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsc.todo_app.api.dto.task.CreateTaskRequest;
import com.lsc.todo_app.api.dto.task.TaskDTO;
import com.lsc.todo_app.api.dto.task.TaskPageDTO;
import com.lsc.todo_app.api.dto.task.UpdateTaskRequest;
import com.lsc.todo_app.api.util.URIUtil;
import com.lsc.todo_app.domain.service.TaskService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequestMapping("/api/tasks")
@RestController
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody CreateTaskRequest task) {
        TaskDTO newTask = taskService.createTask(task);
        URI location = URIUtil.createNewURIById(newTask.getId());

        return ResponseEntity.created(location).body(newTask);
    }
    
    @GetMapping
    public ResponseEntity<TaskPageDTO> readTasks(Pageable pageable) {
        TaskPageDTO taskPage = taskService.readTasks(pageable);
        return ResponseEntity.ok().body(taskPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> readTasksById(@PathVariable Long id) {
        TaskDTO task = taskService.readTaskById(id);
        return ResponseEntity.ok().body(task);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest task) {
        TaskDTO updatedTask = taskService.updateTask(id, task);        
        return ResponseEntity.ok().body(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }    
}