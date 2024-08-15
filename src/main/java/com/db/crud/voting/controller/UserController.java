package com.db.crud.voting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:5173/", "https://votacao-front.onrender.com"})
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<UserResponse> getSprecificUser(@RequestBody String cpf) {
        log.info("Requested Specific User!");
        var body = userService.getUser(cpf);
        log.info("Request Sucessfull!");

        return ResponseEntity.status(HttpStatus.OK).body(body); 
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest userRegisterRequest) {
        log.info("Requested Create User with Name: ", userRegisterRequest.firstName());
        var body = userService.register(userRegisterRequest);
        log.info("Request Sucessfull!");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
    
}
