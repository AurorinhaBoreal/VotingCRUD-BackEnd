package com.db.crud.voting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.request.VoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.VoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.service.AgendaService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
    public ResponseEntity<VoteResponse> voteAgenda(@RequestBody @Valid VoteRequest addVoteRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(agendaService.addVote(addVoteRequest));
    }

    @PutMapping("/end/{question}")
    public ResponseEntity<Void> endAgendaEarly(@PathVariable("question") String question) {        
        agendaService.endAgendaEarly(question);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{question}")
    public ResponseEntity<Void> removeAgenda(@PathVariable("question") String question) {
        agendaService.removeAgenda(question);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

