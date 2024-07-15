package com.db.crud.voting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.model.Agenda;
import com.db.crud.voting.service.agenda.AgendaService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/agenda")
public class AgendaController {
    
    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public ResponseEntity<List<Agenda>> getEndedAgendas() {
        var body = agendaService.getEndedAgendas();
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Agenda>> getActiveAgendas() {
        var body = agendaService.getActiveAgendas();
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/c")
    public ResponseEntity<AgendaResponse> createAgenda(@RequestBody @Valid AgendaRequest agendaRequest) {
        var body = agendaService.createAgenda(agendaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}

