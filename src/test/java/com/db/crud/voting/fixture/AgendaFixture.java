package com.db.crud.voting.fixture;

import java.time.LocalDateTime;

import com.db.crud.voting.dto.mapper.AgendaMapper;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.enums.Category;
import com.db.crud.voting.enums.converters.CategoryConverter;
import com.db.crud.voting.model.Agenda;

public class AgendaFixture {

    static CategoryConverter categoryConverter = new CategoryConverter();

    public static AgendaRequest AgendaDTOValid() {
        return AgendaRequest.builder()
            .category("S")
            .question("I should bet on Palmeiras?")
            .cpf("33092209079")
            .duration(10)
            .build();
    }

    public static AgendaRequest AgendaDTOValid2() {
        return AgendaRequest.builder()
            .category("T")
            .question("I should buy a Kindle?")
            .cpf("33092209079")
            .duration(30)
            .build();
    }

    public static AgendaRequest AgendaDTOInvalid() {
        return AgendaRequest.builder()
            .category("")
            .question("")
            .duration(null)
            .build();
    }

    public static Agenda AgendaEntityValid() {
        Category category = categoryConverter.convertToEntityAttribute("S");
        LocalDateTime finishOn = LocalDateTime.now().plusMinutes(AgendaDTOValid().duration());
        return AgendaMapper.dtoToAgenda(AgendaDTOValid(), category, finishOn);
    }

    public static Agenda AgendaEntityValid2() {
        Category category = categoryConverter.convertToEntityAttribute("T");
        LocalDateTime finishOn = LocalDateTime.now().plusMinutes(AgendaDTOValid().duration());
        return AgendaMapper.dtoToAgenda(AgendaDTOValid2(), category, finishOn);
    }

    public static Agenda AgendaEntityInvalid() {
        Category category = categoryConverter.convertToEntityAttribute("");
        LocalDateTime finishOn = LocalDateTime.now().plusMinutes(AgendaDTOValid().duration());
        return AgendaMapper.dtoToAgenda(AgendaDTOInvalid(), category, finishOn);
    }
}
