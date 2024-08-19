package com.db.crud.voting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.db.crud.voting.dto.response.VoteResponse;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    
    @Mapping(source = "userCpf", target = "userCpf")
    VoteResponse voteToDto(String userCpf);
}
