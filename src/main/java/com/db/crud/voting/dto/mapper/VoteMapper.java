package com.db.crud.voting.dto.mapper;

import com.db.crud.voting.dto.response.AddVoteResponse;

public interface VoteMapper {
    
    static AddVoteResponse voteToDto(boolean voteContabilized, String userCpf) {
        return AddVoteResponse.builder()
            .voteContabilized(voteContabilized)
            .userCpf(userCpf)
            .build();
    }
}
