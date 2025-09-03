package com.lsc.todo_app.utils.domain;

import com.lsc.todo_app.domain.entity.User;

public class UserDomainDataFactory {

    private Long id;
    private String name;
    private String email;
    private String password;

    public UserDomainDataFactory(Long id, String name, String email, String password) {
        this.id = (id != null) ? id : 1L;
        this.name = (name != null) ? name : "John Purple";
        this.email = (email != null) ? email : "john.purple@gmail.com";
        this.password = (password != null) ? password : "mysecret123";
    }

    public UserDomainDataFactory() {
        this.id = 1L;
        this.name = "John Purple";
        this.email = "john.purple@gmail.com";
        this.password = "mysecret123";
    }

    public User createUserWithoutTasks() {
        User user = new User();

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }
}
