package com.db.crud.voting.service.agenda;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.AgendaMapper;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.model.Agenda;
import com.db.crud.voting.repository.AgendaRepository;

@Service
public class AgendaServiceImpl implements AgendaService {
    
    AgendaRepository agendaRepository;

    public AgendaServiceImpl(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public List<Agenda> getEndedAgendas() {
        return agendaRepository.findByHasEnded(false);
    }

    public List<Agenda> getActiveAgendas() {
        return agendaRepository.findByHasEnded(true);
    }

    public AgendaResponse createAgenda(AgendaRequest agendaRequest) {
        Optional<Agenda> agenda = agendaRepository.findByQuestion(agendaRequest.question());
        if (agenda.isPresent()) {
            throw new RuntimeException("This agenda was already created!");
        }
        Agenda agendaCreated = AgendaMapper.dtoToAgenda(agendaRequest);
        agendaRepository.save(agendaCreated);
        return AgendaMapper.agendaToDto(agendaCreated);
    }
}
