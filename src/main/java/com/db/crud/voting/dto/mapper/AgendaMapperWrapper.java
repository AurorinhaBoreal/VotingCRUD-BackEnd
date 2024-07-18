package com.db.crud.voting.dto.mapper;

import org.mapstruct.Mapper;

import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.enums.Category;
import com.db.crud.voting.model.Agenda;

// This class is create just to execute tests
@Mapper(componentModel = "spring")
public class AgendaMapperWrapper {
    public Agenda dtoToAgenda(AgendaRequest agendaRequest, Category agendaCategory) {
        return AgendaMapper.dtoToAgenda(agendaRequest, agendaCategory);
    }
}
