package com.db.crud.voting.dto.mapper;

import org.mapstruct.Mapper;

import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.model.User;

// This class is create just to execute tests
@Mapper(componentModel = "spring")
public class UserMapperWrapper {
    public User dtoToUser(UserRequest userRequest, UserType userType) {
        return UserMapper.dtoToUser(userRequest, userType);
    }
}
