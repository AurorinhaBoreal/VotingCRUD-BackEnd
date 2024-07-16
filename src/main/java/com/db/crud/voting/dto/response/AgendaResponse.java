package com.db.crud.voting.dto.response;

import com.db.crud.voting.enums.Category;

import lombok.Builder;

@Builder
public record AgendaResponse(
    Category category,
    String question,
    Integer yesVotes,
    Integer noVotes,
    Integer totalVotes,
    Integer duration,
    boolean hasEnded) {
    
}
