package com.lsc.todo_app.api.dto.user;

import java.util.List;
import java.util.stream.Collectors;

import com.lsc.todo_app.api.dto.task.TaskSumarryDTO;
import com.lsc.todo_app.domain.entity.User;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private List<TaskSumarryDTO> task;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.task = user.getTask().stream()
                .map(TaskSumarryDTO::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TaskSumarryDTO> getTask() {
        return task;
    }

    public void setTask(List<TaskSumarryDTO> task) {
        this.task = task;
    }
}
