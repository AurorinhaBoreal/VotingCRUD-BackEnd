package com.db.crud.voting.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AddVoteRequest(
    @NotBlank(message = "Inform the specified agenda")
    String question,

    @NotBlank(message = "Inform the user who voted")
    String cpf,

    @NotBlank(message = "Inform the vote")
    String vote) {
}
