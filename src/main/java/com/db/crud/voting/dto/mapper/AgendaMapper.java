package com.db.crud.voting.dto.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.model.Agenda;

@Mapper(componentModel = "spring")
public interface AgendaMapper {
    
    AgendaResponse agendaToDto(Agenda agenda);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "yesVotes", ignore = true)
    @Mapping(target = "noVotes", ignore = true)
    @Mapping(target = "totalVotes", ignore = true)
    @Mapping(target = "hasEnded", ignore = true)
    @Mapping(target = "usersVoted", ignore = true)
    @Mapping(source = "agendaRequest.category", target = "category")
    @Mapping(source = "agendaRequest.duration", target = "duration")
    @Mapping(source = "agendaRequest.question", target = "question")
    @Mapping(source = "agendaFinish", target = "finishOn")
    Agenda dtoToAgenda(AgendaRequest agendaRequest, LocalDateTime agendaFinish);
}
