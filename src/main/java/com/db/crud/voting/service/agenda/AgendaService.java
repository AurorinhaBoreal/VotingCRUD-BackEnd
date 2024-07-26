package com.db.crud.voting.service.agenda;

import java.util.List;

import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;

public interface AgendaService {
    
    public List<AgendaResponse> getEndedAgendas();

    public List<AgendaResponse> getActiveAgendas();

    public AgendaResponse createAgenda(AgendaRequest agendaRequest);

    public AddVoteResponse addVote(AddVoteRequest addVoteRequest);
}
