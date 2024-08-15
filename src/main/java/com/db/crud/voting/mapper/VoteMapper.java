package com.db.crud.voting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.db.crud.voting.dto.response.AddVoteResponse;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    
    @Mapping(source = "userCpf", target = "userCpf")
    AddVoteResponse voteToDto(String userCpf);
}
