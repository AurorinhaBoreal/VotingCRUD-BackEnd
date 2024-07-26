package com.db.crud.voting.dto.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;

import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.enums.Category;
import com.db.crud.voting.model.Agenda;

@Mapper(componentModel = "spring")
public interface AgendaMapper {
    
    static AgendaResponse agendaToDto(Agenda agenda) {
        return AgendaResponse.builder()
            .category(agenda.getCategory())
            .duration(agenda.getDuration())
            .question(agenda.getQuestion())
            .hasEnded(agenda.isHasEnded())
            .noVotes(agenda.getNoVotes())
            .yesVotes(agenda.getYesVotes())
            .totalVotes(agenda.getTotalVotes())
            .createdOn(agenda.getCreatedOn().toString())
            .build();
    }

    static Agenda dtoToAgenda(AgendaRequest agendaRequest, Category agendaCategory, LocalDateTime agendaFinish) {
        return Agenda.builder()
            .category(agendaCategory)
            .duration(agendaRequest.duration())
            .question(agendaRequest.question())
            .finishOn(agendaFinish)
            .build();
    }
}
