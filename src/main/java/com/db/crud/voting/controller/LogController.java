package com.db.crud.voting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.service.logs.LogService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/log")
@CrossOrigin(origins = {"http://localhost:3000/", "https://votacao-front.onrender.com"})
public class LogController {
    
    
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<LogResponse>> getLogs() {
        var body = logService.getLogs();
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    
}
