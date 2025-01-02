package com.lsc.todo_app.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lsc.todo_app.api.dto.user.UserRequest;
import com.lsc.todo_app.api.dto.user.UserDTO;
import com.lsc.todo_app.api.dto.user.UserPageDTO;
import com.lsc.todo_app.api.dto.user.UserSumarryDTO;
import com.lsc.todo_app.domain.entity.User;
import com.lsc.todo_app.domain.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userRepository.save(user);
        return new UserDTO(user);
    }

    public UserPageDTO readUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserSumarryDTO> userDTOs = userPage.getContent().stream()
        .map(user -> {
            return new UserSumarryDTO(user);
        }).collect(Collectors.toList());

        return new UserPageDTO(userDTOs, userPage.getNumber(), userPage.getTotalPages(), userPage.getTotalElements(), userPage.getSize());
    }

    public UserDTO updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }

        User userUpdated = userRepository.save(user);
        return new UserDTO(userUpdated);
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }
}
