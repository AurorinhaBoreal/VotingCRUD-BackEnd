package com.db.crud.voting.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.model.Agenda;

@Mapper(componentModel = "spring")
public interface AgendaMapper {
    
    AgendaResponse agendaToDto(Agenda agenda);

    @Mapping(source = "agendaFinish", target = "finishOn")
    Agenda dtoToAgenda(AgendaRequest agendaRequest, LocalDateTime agendaFinish);
}
