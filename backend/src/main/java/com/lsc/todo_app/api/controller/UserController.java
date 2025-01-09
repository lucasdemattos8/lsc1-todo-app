package com.lsc.todo_app.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsc.todo_app.api.dto.user.UserRequest;
import com.lsc.todo_app.api.util.URIUtil;
import com.lsc.todo_app.api.dto.user.UserDTO;
import com.lsc.todo_app.api.dto.user.UserPageDTO;
import com.lsc.todo_app.domain.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequestMapping("/api/users")
@RestController
public class UserController {
    
    @Autowired
    private UserService userService;
    private URIUtil uriUtil;
    
    public UserController(UserService userService, URIUtil uriUtil) {
        this.userService = userService;
        this.uriUtil = uriUtil;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequest user) {
        UserDTO newUser = userService.createUser(user);
        URI location = uriUtil.createNewURIById(newUser.getId());
        
        return ResponseEntity.created(location).body(newUser);
    }

    @GetMapping
    public ResponseEntity<UserPageDTO> readUsers(Pageable pageable) {
        UserPageDTO userPage = userService.readUsers(pageable);
        return ResponseEntity.ok().body(userPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> readUsersById (@PathVariable Long id) {
        UserDTO user = userService.readUserById(id);
        return ResponseEntity.ok().body(user);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserRequest user) {
        UserDTO updatedUser = userService.updateUser(id, user);        
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
