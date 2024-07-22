package com.db.crud.voting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.service.agenda.AgendaService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/agenda")
@CrossOrigin(origins = "http://localhost:3000/")
public class AgendaController {
    
    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public ResponseEntity<List<AgendaResponse>> getEndedAgendas() {
        var body = agendaService.getEndedAgendas();
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<AgendaResponse>> getActiveAgendas() {
        var body = agendaService.getActiveAgendas();
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping
    public ResponseEntity<AgendaResponse> createAgenda(@RequestBody @Valid AgendaRequest agendaRequest) {
        var body = agendaService.createAgenda(agendaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/vote")
    public ResponseEntity<AddVoteResponse> voteAgenda(@RequestBody @Valid AddVoteRequest addVoteRequest) {
        var body = agendaService.addVote(addVoteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PutMapping("/finalize")
    public ResponseEntity<String> finishAgenda(@RequestBody String question) {
        var body = agendaService.finishAgenda(question);        
        return ResponseEntity.ok().body(body);
    }
}

