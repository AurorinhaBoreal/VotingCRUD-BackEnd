package com.db.crud.voting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:5173/", "https://votacao-front.onrender.com"})
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/specific")
    public ResponseEntity<UserResponse> getSprecificUser(@RequestBody String cpf) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserResponse(cpf)); 
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }
  
    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest userRegisterRequest) {        
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRegisterRequest));
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Void> allowAccess(@RequestBody @Valid String cpf) {
        userService.allowAccess(cpf);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> removeUser(@PathVariable("cpf") String cpf) {
        userService.removeUser(cpf);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}