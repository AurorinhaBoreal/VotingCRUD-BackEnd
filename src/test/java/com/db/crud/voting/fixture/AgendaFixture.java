package com.db.crud.voting.fixture;

import java.time.LocalDateTime;

import com.db.crud.voting.dto.mapper.AgendaMapper;
import com.db.crud.voting.dto.mapper.AgendaMapperImpl;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.enums.Category;
import com.db.crud.voting.model.Agenda;

public class AgendaFixture {

    public static AgendaRequest AgendaDTOValid() {
        return AgendaRequest.builder()
            .category(Category.SPORTS)
            .question("I should bet on Palmeiras?")
            .cpf("33092209079")
            .duration(10)
            .build();
    }

    public static AgendaRequest AgendaDTOValid2() {
        return AgendaRequest.builder()
            .category(Category.TECHNOLOGY)
            .question("I should buy a Kindle?")
            .cpf("33092209079")
            .duration(30)
            .build();
    }

    public static AgendaRequest AgendaDTOInvalid() {
        return AgendaRequest.builder()
            .category(null)
            .question("")
            .duration(null)
            .build();
    }

    public static Agenda AgendaEntityValid() {
        AgendaMapper agendaMapper = new AgendaMapperImpl();
        LocalDateTime finishOn = LocalDateTime.now().plusMinutes(AgendaDTOValid().duration());
        return agendaMapper.dtoToAgenda(AgendaDTOValid(), finishOn);
    }

    public static Agenda AgendaEntityValid2() {
        AgendaMapper agendaMapper = new AgendaMapperImpl();
        LocalDateTime finishOn = LocalDateTime.now().plusMinutes(AgendaDTOValid().duration());
        return agendaMapper.dtoToAgenda(AgendaDTOValid2(), finishOn);
    }

    public static Agenda AgendaEntityInvalid() {
        AgendaMapper agendaMapper = new AgendaMapperImpl();
        LocalDateTime finishOn = LocalDateTime.now().plusMinutes(AgendaDTOValid().duration());
        return agendaMapper.dtoToAgenda(AgendaDTOInvalid(), finishOn);
    }
}
