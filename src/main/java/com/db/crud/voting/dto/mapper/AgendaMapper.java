package com.db.crud.voting.dto.mapper;

import org.mapstruct.Mapper;

import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.model.Agenda;

@Mapper(componentModel = "spring")
public interface AgendaMapper {
    
    static AgendaResponse agendaToDto(Agenda agenda) {
        return AgendaResponse.builder()
            .category(agenda.getCategory())
            .duration(agenda.getDuration())
            .question(agenda.getQuestion())
            .hasEnded(false)
            .noVotes(0)
            .yesVotes(0)
            .totalVotes(0)
            .build();
    }

    static Agenda dtoToAgenda(AgendaRequest agendaRequest) {
        return Agenda.builder()
            .category(agendaRequest.category())
            .duration(agendaRequest.duration())
            .question(agendaRequest.question())
            .build();
    }
}
