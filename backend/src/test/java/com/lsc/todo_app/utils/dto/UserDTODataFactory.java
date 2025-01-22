package com.lsc.todo_app.utils.dto;

import com.lsc.todo_app.api.dto.user.UserSumarryDTO;

public class UserDTODataFactory {

    private Long id;
    private String name;
    private String email;

    public UserDTODataFactory(Long id, String name, String email, String password) {
        this.id = (id != null) ? id : 1L;
        this.name = (name != null) ? name : "John Purple";
        this.email = (email != null) ? email : "john.purple@gmail.com";
    }

    public UserDTODataFactory() {
        this.id = 1L;
        this.name = "John Purple";
        this.email = "john.purple@gmail.com";
    }

    public UserSumarryDTO UserSumarryDTO() {
        UserSumarryDTO testUser = new UserSumarryDTO();
        testUser.setId(id);
        testUser.setName(name);
        testUser.setEmail(email);

        return testUser;
    }
}
