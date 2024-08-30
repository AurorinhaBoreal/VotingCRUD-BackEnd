package com.db.crud.voting.service;

import java.util.List;

import com.db.crud.voting.dto.request.VoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.VoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;

public interface AgendaService {
    
    List<AgendaResponse> getEndedAgendas();

    List<AgendaResponse> getActiveAgendas();

    AgendaResponse createAgenda(AgendaRequest agendaRequest);

    VoteResponse addVote(VoteRequest addVoteRequest);

    void endAgendaEarly(String question);

    void removeAgenda(String question);
}
