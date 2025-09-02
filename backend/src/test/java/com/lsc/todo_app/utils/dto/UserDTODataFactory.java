package com.lsc.todo_app.utils.dto;

import com.lsc.todo_app.api.dto.user.UserRequest;
import com.lsc.todo_app.api.dto.user.UserSumarryDTO;

public class UserDTODataFactory {

    private Long id;
    private String name;
    private String email;
    private String password;

    public UserDTODataFactory(Long id, String name, String email, String password) {
        this.id = (id != null) ? id : 1L;
        this.name = (name != null) ? name : "John Purple";
        this.email = (email != null) ? email : "john.purple@gmail.com";
        this.password = (password != null) ? password : "password";
    }

    public UserDTODataFactory() {
        this.id = 1L;
        this.name = "John Purple";
        this.email = "john.purple@gmail.com";
    }

    public UserSumarryDTO userSumarryDTO() {
        UserSumarryDTO testUser = new UserSumarryDTO();
        testUser.setId(id);
        testUser.setName(name);
        testUser.setEmail(email);

        return testUser;
    }

    public UserRequest userRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName(name);
        userRequest.setEmail(email);
        userRequest.setPassword(password);

        return userRequest;
    }
}
