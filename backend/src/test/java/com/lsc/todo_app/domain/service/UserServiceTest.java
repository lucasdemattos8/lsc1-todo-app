package com.lsc.todo_app.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.lsc.todo_app.api.dto.user.UserDTO;
import com.lsc.todo_app.api.dto.user.UserPageDTO;
import com.lsc.todo_app.api.dto.user.UserRequest;
import com.lsc.todo_app.api.dto.user.UserSummaryDTO;
import com.lsc.todo_app.domain.entity.User;
import com.lsc.todo_app.domain.repository.UserRepository;
import com.lsc.todo_app.utils.domain.UserDomainDataFactory;
import com.lsc.todo_app.utils.dto.UserDTODataFactory;

@DisplayName("UserService - CRUD Unit Tests")
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;

    private UserDTODataFactory userDTODataFactory;
    private UserDomainDataFactory userDomainDataFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTODataFactory = new UserDTODataFactory();
        userDomainDataFactory = new UserDomainDataFactory();
    }

    @Test
    @DisplayName("Should correctly create an User and call save in repository")
    void shouldCreateCorrectlyUser() {
        UserRequest createUser = userDTODataFactory.userRequest();
        User createdUser = userDomainDataFactory.createUserWithoutTasks();
        createdUser.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(createdUser);

        UserDTO createdUserDTO = userService.createUser(createUser);

        assertNotNull(createdUserDTO);
        assertEquals(createdUser.getId(), createdUserDTO.getId());
        assertEquals(createdUser.getName(), createdUserDTO.getName());
        assertEquals(createdUser.getEmail(), createdUserDTO.getEmail());
        assertTrue(createdUser.getTasks().isEmpty());
    }

    @Test
    @DisplayName("Should return a pageable data by findAll from User repository")
    void shouldReturnAPagebleDataFromDatabase() {
        User firstUser = userDomainDataFactory.createUserWithoutTasks();
        User secondUser = userDomainDataFactory.createUserWithoutTasks();
        User thirdUser = userDomainDataFactory.createUserWithoutTasks();

        firstUser.setId(1L);
        secondUser.setId(2L);
        thirdUser.setId(3L);

        final List<User> userList = List.of(firstUser, secondUser, thirdUser);
        final Page<User> pageableUsers = new PageImpl<>(userList);
        final Pageable pageable = PageRequest.of(
            0, 10, Sort.by("id").descending());

        when(userRepository.findAll(any(Pageable.class))).thenReturn(pageableUsers);

        final UserPageDTO response = userService.readUsers(pageable);

        final List<UserSummaryDTO> responseContent = response.getUsers();
        final int expectedElements = 3;

        assertNotNull(response);
        assertNotNull(responseContent);
        assertEquals(expectedElements, response.getTotalElements());
        assertEquals(expectedElements, responseContent.size());
    }

    @Test
    @DisplayName("Should return correctly a UserDTO data by ID from repository")
    void shouldReturnCorrectlyAUserDTOFromRepository() {
        final User user = userDomainDataFactory.createUserWithoutTasks();
        final Long idToFind = 1L;

        when(userRepository.findById(idToFind)).thenReturn(
            Optional.of(user));

        final UserDTO response = userService.readUserById(idToFind);
        
        assertEquals(idToFind, response.getId());
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getTasks().size(), response.getTasks().size());
    }

    @Test
    @DisplayName("Should throws an Exception when User doesnt exists by the provided ID from repository")
    void shouldThrowsExceptionWhenInFindUserDoesntExistsTheProvidedID() {
        final User user = userDomainDataFactory.createUserWithoutTasks();
        final Long idToFind = 99L;

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        final Exception exceptionResponse = assertThrows(
                RuntimeException.class, () -> userService.readUserById(idToFind));
        
        final String expectedMessage = "User not found with id " + idToFind;

        assertEquals(expectedMessage, exceptionResponse.getMessage());
    }

    @Test
    @DisplayName("Should update an User correctly when founded by ID from repository")
    void shouldUpdateUserWhenCorrectlyFound() {
        final Long idToUpdate = 1L;
        User user = userDomainDataFactory.createUserWithoutTasks();
        user.setId(idToUpdate);

        final UserRequest updateRequestDTO = new UserRequest("Tester", "teste@gmail.com", "teste123");

        when(userRepository.findById(idToUpdate)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final UserDTO response = userService.updateUser(idToUpdate, updateRequestDTO);

        assertEquals(idToUpdate, response.getId());
        assertEquals(updateRequestDTO.getName(), response.getName());
        assertEquals(updateRequestDTO.getEmail(), response.getEmail());
        assertEquals(user.getTasks().size(), response.getTasks().size());
    }

    @Test
    @DisplayName("Should update an User correctly when password is not provided")
    void shouldUpdateUserWhenPasswordIsNotProvided() {
        final Long idToUpdate = 1L;
        User user = userDomainDataFactory.createUserWithoutTasks();
        user.setId(idToUpdate);
        String originalPassword = user.getPassword();

        final UserRequest updateRequestDTO = new UserRequest("Tester", "teste@gmail.com", null);

        when(userRepository.findById(idToUpdate)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final UserDTO response = userService.updateUser(idToUpdate, updateRequestDTO);

        assertEquals(idToUpdate, response.getId());
        assertEquals(updateRequestDTO.getName(), response.getName());
        assertEquals(updateRequestDTO.getEmail(), response.getEmail());
        assertEquals(originalPassword, user.getPassword());
        assertEquals(user.getTasks().size(), response.getTasks().size());
    }

    @Test
    @DisplayName("Should update an User correctly when name is not provided")
    void shouldUpdateUserWhenNameIsNotProvided() {
        final Long idToUpdate = 1L;
        User user = userDomainDataFactory.createUserWithoutTasks();
        user.setId(idToUpdate);
        String originalName = user.getName();

        final UserRequest updateRequestDTO = new UserRequest(null, "teste@gmail.com", "teste123");

        when(userRepository.findById(idToUpdate)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final UserDTO response = userService.updateUser(idToUpdate, updateRequestDTO);

        assertEquals(idToUpdate, response.getId());
        assertEquals(originalName, response.getName());
        assertEquals(updateRequestDTO.getEmail(), response.getEmail());
        assertEquals(updateRequestDTO.getPassword(), user.getPassword());
        assertEquals(user.getTasks().size(), response.getTasks().size());
    }

    @Test
    @DisplayName("Should update an User correctly when email is not provided")
    void shouldUpdateUserWhenEmailIsNotProvided() {
        final Long idToUpdate = 1L;
        User user = userDomainDataFactory.createUserWithoutTasks();
        user.setId(idToUpdate);
        String originalEmail = user.getEmail();

        final UserRequest updateRequestDTO = new UserRequest("Tester", null, "teste123");

        when(userRepository.findById(idToUpdate)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final UserDTO response = userService.updateUser(idToUpdate, updateRequestDTO);

        assertEquals(idToUpdate, response.getId());
        assertEquals(updateRequestDTO.getName(), response.getName());
        assertEquals(originalEmail, response.getEmail());
        assertEquals(updateRequestDTO.getPassword(), user.getPassword());
        assertEquals(user.getTasks().size(), response.getTasks().size());
    }

    @Test
    @DisplayName("Should throws an Exception when User doesnt exists by the provided ID from repository when Update User")
    void shouldThrowsExceptionWhenInUpdateUserDoesntExistsTheProvidedID() {
        final Long nonexistingID = 99L;
        final UserRequest updateRequestDTO = new UserRequest("Tester", "teste@gmail.com", "teste123");

        final Exception exceptionResponse = assertThrows(
                RuntimeException.class, () -> userService.updateUser(nonexistingID, updateRequestDTO));
        
        final String expectedMessage = "User not found with id " + nonexistingID;

        assertEquals(expectedMessage, exceptionResponse.getMessage());
    }

    @Test
    @DisplayName("Should delete a User when the ID exists")
    void shouldDeleteCorrectlyUserWhenTheIDExists() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should throws an Exception when User doesnt exists by the provided ID from repository when an Operation to Delete User")
    void shouldThrowsExceptionWhenInDeleteUserDoesntExistsTheProvidedID() {
        final Long idToDelete = 99L;
        final String expectedMessage = "User not found with id " + idToDelete;

        when(userRepository.existsById(idToDelete)).thenReturn(false);
        
        final Exception exceptionResponse = assertThrows(
                RuntimeException.class, () -> userService.deleteUser(idToDelete));
        
        assertEquals(expectedMessage, exceptionResponse.getMessage());
        verify(userRepository, never()).deleteById(anyLong());
    }
}
