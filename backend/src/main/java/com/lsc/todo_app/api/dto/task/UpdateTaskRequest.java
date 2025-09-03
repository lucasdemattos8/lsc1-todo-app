package com.lsc.todo_app.api.dto.task;

import com.lsc.todo_app.domain.entity.enums.Status;

public class UpdateTaskRequest {
    private String title;
    private String description;
    private Status status;

    public UpdateTaskRequest() {
    }

    public UpdateTaskRequest(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    } 
}
