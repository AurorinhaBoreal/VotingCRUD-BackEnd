package com.db.crud.voting.dto.response;

import lombok.Builder;

@Builder
public record AddVoteResponse(
    String userCpf) {
}
