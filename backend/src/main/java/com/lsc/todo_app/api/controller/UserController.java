package com.lsc.todo_app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsc.todo_app.api.dto.user.UserRequest;
import com.lsc.todo_app.api.dto.user.UserDTO;
import com.lsc.todo_app.api.dto.user.UserPageDTO;
import com.lsc.todo_app.domain.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/api/users")
@RestController
public class UserController {
    
    @Autowired
    private UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequest user) {
        UserDTO newUser = userService.createUser(user);
        return ResponseEntity.ok().body(newUser);
    }

    @GetMapping
    public ResponseEntity<UserPageDTO> readUsers(Pageable pageable) {
        UserPageDTO userPage = userService.readUsers(pageable);
        return ResponseEntity.ok().body(userPage);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestParam Long id, @RequestBody UserRequest user) {
        UserDTO updatedUser = userService.updateUser(id, user);        
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@RequestParam Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
