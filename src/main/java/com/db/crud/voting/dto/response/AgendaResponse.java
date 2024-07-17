package com.db.crud.voting.dto.response;

import java.util.List;

import com.db.crud.voting.enums.Category;
import com.db.crud.voting.model.User;

import lombok.Builder;

@Builder
public record AgendaResponse(
    Category category,
    String question,
    Integer yesVotes,
    Integer noVotes,
    Integer totalVotes,
    Integer duration,
    boolean hasEnded,
    List<User> usersWhoVoted) {
    
}
