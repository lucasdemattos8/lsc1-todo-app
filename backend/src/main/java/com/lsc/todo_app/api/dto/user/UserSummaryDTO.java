package com.lsc.todo_app.api.dto.user;

import com.lsc.todo_app.domain.entity.User;

public class UserSummaryDTO {

    private Long id;
    private String name;
    private String email;

    public UserSummaryDTO() {
    }

    public UserSummaryDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
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
}
