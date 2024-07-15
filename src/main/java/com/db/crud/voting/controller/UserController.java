package com.db.crud.voting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.request.UserLoginRequest;
import com.db.crud.voting.dto.request.UserRegisterRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.service.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    

    @PostMapping
    public ResponseEntity<UserResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        var body = userService.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    
    @PostMapping("/create")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        var body = userService.register(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
    
}
