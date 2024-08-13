package com.db.crud.voting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.service.agenda.AgendaService;

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
    
    private static final String REQUEST_SUCCESSFUL = "Request Successful!";
    private final AgendaService agendaService;

    @GetMapping
    public ResponseEntity<List<AgendaResponse>> getEndedAgendas() {
        log.info("Requested Get Ended Agendas!");
        agendaService.finishAgenda();
        var body = agendaService.getEndedAgendas();
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<AgendaResponse>> getActiveAgendas() {
        log.info("Requested Get Active Agendas!");
        agendaService.finishAgenda();
        var body = agendaService.getActiveAgendas();
        log.info(REQUEST_SUCCESSFUL);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping
    public ResponseEntity<AgendaResponse> createAgenda(@RequestBody @Valid AgendaRequest agendaRequest) {
        log.info("Requested Create Agenda: ", agendaRequest);
        var body = agendaService.createAgenda(agendaRequest);
        log.info(REQUEST_SUCCESSFUL);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/vote")
    public ResponseEntity<AddVoteResponse> voteAgenda(@RequestBody @Valid AddVoteRequest addVoteRequest) {
        log.info("Requested Vote!");
        agendaService.finishAgenda();
        var body = agendaService.addVote(addVoteRequest);
        log.info(REQUEST_SUCCESSFUL);
        
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}

