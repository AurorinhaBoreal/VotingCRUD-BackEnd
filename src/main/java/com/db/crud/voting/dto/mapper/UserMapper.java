package com.db.crud.voting.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToDto(User user);

    @Mapping(target = "id", ignore = true)
    User dtoToUser(UserRequest userDTO);
}
