package com.db.crud.voting.dto.response;

import lombok.Builder;

@Builder
public record VoteResponse(
    String userCpf) {
}
