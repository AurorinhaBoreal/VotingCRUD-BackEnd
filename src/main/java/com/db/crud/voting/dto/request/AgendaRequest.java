package com.db.crud.voting.dto.request;

import com.db.crud.voting.enums.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AgendaRequest(
    @NotNull(message = "Inform a Category")
    Category category,

    @NotBlank(message = "Inform a question for the agenda")
    String question,

    Integer duration){
}
