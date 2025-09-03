package com.lsc.todo_app.api.dto.task;

import java.time.LocalDateTime;

import com.lsc.todo_app.api.dto.user.UserSummaryDTO;
import com.lsc.todo_app.domain.entity.Task;
import com.lsc.todo_app.domain.entity.enums.Status;

public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserSummaryDTO user;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, String description, Status status, LocalDateTime createdAt, LocalDateTime updatedAt, UserSummaryDTO user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
    }

    public TaskDTO(Task task, UserSummaryDTO user) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
        this.user = user;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public UserSummaryDTO getUser() {
        return user;
    }
    public void setUser(UserSummaryDTO user) {
        this.user = user;
    }
}
