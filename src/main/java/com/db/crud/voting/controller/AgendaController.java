package com.db.crud.voting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.service.AgendaService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/agenda")
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:5173/", "https://votacao-front.onrender.com"})
public class AgendaController {
    
    private final AgendaService agendaService;

    @GetMapping
    public ResponseEntity<List<AgendaResponse>> getEndedAgendas() {
        return ResponseEntity.status(HttpStatus.OK).body(agendaService.getEndedAgendas());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<AgendaResponse>> getActiveAgendas() {
        return ResponseEntity.status(HttpStatus.OK).body(agendaService.getActiveAgendas());
    }

    @PostMapping
    public ResponseEntity<AgendaResponse> createAgenda(@RequestBody @Valid AgendaRequest agendaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.createAgenda(agendaRequest));
    }

    @PostMapping("/vote")
    public ResponseEntity<AddVoteResponse> voteAgenda(@RequestBody @Valid AddVoteRequest addVoteRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(agendaService.addVote(addVoteRequest));
    }
}

