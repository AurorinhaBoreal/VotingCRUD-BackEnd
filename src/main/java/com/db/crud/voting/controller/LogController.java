package com.db.crud.voting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.service.logs.LogService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;



@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/log")
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:5173/", "https://votacao-front.onrender.com"})
public class LogController {
    
    
    private final LogService logService;

    @GetMapping
    public ResponseEntity<List<LogResponse>> getLogs() {
        log.info("Logs Requested!");
        var body = logService.getLogs();
        log.info("Request Sucessfull!");
        
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    
}
