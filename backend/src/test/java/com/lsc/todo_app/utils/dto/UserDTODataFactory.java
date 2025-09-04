package com.lsc.todo_app.utils.dto;

import java.util.ArrayList;

import com.lsc.todo_app.api.dto.task.TaskSummaryDTO;
import com.lsc.todo_app.api.dto.user.UserDTO;
import com.lsc.todo_app.api.dto.user.UserRequest;
import com.lsc.todo_app.api.dto.user.UserSummaryDTO;

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

    public UserDTO userDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setName(name);
        userDTO.setEmail(email);
        userDTO.setTasks(new ArrayList<TaskSummaryDTO>());

        return userDTO;
    }

    public UserSummaryDTO userSummaryDTO() {
        UserSummaryDTO userSummaryDTO = new UserSummaryDTO();
        userSummaryDTO.setId(id);
        userSummaryDTO.setName(name);
        userSummaryDTO.setEmail(email);

        return userSummaryDTO;
    }

    public UserRequest userRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName(name);
        userRequest.setEmail(email);
        userRequest.setPassword(password);

        return userRequest;
    }
}
