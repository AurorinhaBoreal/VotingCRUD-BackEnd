package com.db.crud.voting.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddVoteRequest(
    @NotBlank(message = "Inform the specified agenda")
    String question,

    @NotBlank(message = "Inform the user who voted")
    String cpf,
    boolean yes,
    boolean no) {
}
