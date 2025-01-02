package com.lsc.todo_app.api.dto.task;

import com.lsc.todo_app.domain.entity.Task;

public class TaskSumarryDTO {

    private Long id;
    private String title;
    private String description;

    public TaskSumarryDTO() {
    }

    public TaskSumarryDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
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
}
