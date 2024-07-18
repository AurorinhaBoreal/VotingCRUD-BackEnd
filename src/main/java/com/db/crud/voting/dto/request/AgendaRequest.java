package com.db.crud.voting.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AgendaRequest(
    @NotBlank(message = "Inform a Category")
    String category,

    @NotBlank(message = "Inform a question for the agenda")
    String question,

    @NotBlank(message = "Please inform a cpf")
    String cpf,

    Integer duration){
}
