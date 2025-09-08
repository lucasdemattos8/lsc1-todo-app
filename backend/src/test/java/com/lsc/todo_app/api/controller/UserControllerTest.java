package com.lsc.todo_app.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.lsc.todo_app.api.dto.user.UserDTO;
import com.lsc.todo_app.api.dto.user.UserPageDTO;
import com.lsc.todo_app.api.dto.user.UserRequest;
import com.lsc.todo_app.api.dto.user.UserSummaryDTO;
import com.lsc.todo_app.api.util.URIUtil;
import com.lsc.todo_app.domain.service.UserService;
import com.lsc.todo_app.utils.dto.UserDTODataFactory;

@DisplayName("UserController - CRUD Unit Tests")
public class UserControllerTest {
    
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private URIUtil uriUtil;

    private UserDTODataFactory userDTODataFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTODataFactory = new UserDTODataFactory();
    }

    @DisplayName("Should create a User and return 201 status with non-null UserDTO body")
    @Test
    void shouldCreateCorrectlyUser() {
        final URI expectedURI = URI.create("https://localhost/users/1");
        UserRequest request = userDTODataFactory.userRequest();
        UserDTO userDTO = userDTODataFactory.userDTO();

        when(userService.createUser(request)).thenReturn(userDTO);
        when(uriUtil.createNewURIById(1L)).thenReturn(expectedURI);

        ResponseEntity<UserDTO> response = userController.createUser(request);

        assertNotNull(response, "Expected the response is not null");
        assertEquals(201, response.getStatusCode().value(), "Expected HTTP Status 201 for task creation");

        assertNotNull(response.getBody(), "Expected the response body is not null");

        verify(userService, times(1)).createUser(any(UserRequest.class));
    }

    
    @DisplayName("Should read pageable users and return 200 status with non-null UserPageDTO body")
    @Test
    void shouldReadCorrectlyUsers() {
        UserSummaryDTO userSummaryDTO = userDTODataFactory.userSummaryDTO();

        List<UserSummaryDTO> userList = Arrays.asList(userSummaryDTO);

        UserPageDTO contentPage = new UserPageDTO(userList, 0, 1, 1, 20);

        when(userService.readUsers(any(Pageable.class))).thenReturn(contentPage);

        ResponseEntity<UserPageDTO> response = userController.readUsers(Pageable.unpaged());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        verify(userService, times(1)).readUsers(Pageable.unpaged());
    }

    @DisplayName("Should read a user by ID and return 200 status with non-null UserDTO body")
    @Test
    void shouldReadCorrectlyUser() {
        UserDTO userDTO = userDTODataFactory.userDTO();

        when(userService.readUserById(anyLong())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.readUsersById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        verify(userService, times(1)).readUserById(anyLong());
    }

    @DisplayName("Should update a user by ID and return 200 status with non-null UserDTO body")
    @Test
    void shouldUpdateCorrectlyTask() {
        UserDTO userDTO = userDTODataFactory.userDTO();
        UserRequest request = userDTODataFactory.userRequest();

        when(userService.updateUser(anyLong(), any(UserRequest.class))).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(1L, request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        verify(userService, times(1)).updateUser(anyLong(), any(UserRequest.class));
    }

    @DisplayName("Should delete a task by ID and return 204 status with null body")
    @Test
    void shouldDeleteCorrectlyTask() {
        UserDTO userDTO = userDTODataFactory.userDTO();

        doNothing().when(userService).deleteUser(anyLong());

        ResponseEntity<?> response = userController.deleteUser(userDTO.getId());

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());

        assertNull(response.getBody());

        verify(userService, times(1)).deleteUser(anyLong());
    }


}
