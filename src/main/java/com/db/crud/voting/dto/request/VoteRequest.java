package com.db.crud.voting.dto.request;

import com.db.crud.voting.enums.Vote;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record VoteRequest(
    @NotBlank(message = "Inform the specified agenda")
    String question,

    @NotBlank(message = "Inform the user who voted")
    String cpf,

    @NotNull(message = "Inform the vote")
    Vote vote) {
}
