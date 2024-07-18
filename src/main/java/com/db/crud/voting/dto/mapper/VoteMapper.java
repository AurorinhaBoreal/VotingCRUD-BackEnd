package com.db.crud.voting.dto.mapper;

import org.mapstruct.Mapper;

import com.db.crud.voting.dto.response.AddVoteResponse;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    
    static AddVoteResponse voteToDto(boolean voteContabilized, String userCpf) {
        return AddVoteResponse.builder()
            .voteContabilized(voteContabilized)
            .userCpf(userCpf)
            .build();
    }
}
