package com.db.crud.voting.dto.mapper;

import org.mapstruct.Mapper;

import com.db.crud.voting.dto.request.UserRegisterRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    static UserResponse userToDto(User user) {
        return UserResponse.builder()
                .userType(user.getUserType())
                .firstName(user.getFirstName())
                .surname(user.getSurname())
                .build();
    }

    static User dtoToUser(UserRegisterRequest userDTO) {
        return User.builder()
                .userType(userDTO.userType())
                .firstName(userDTO.firstName())
                .surname(userDTO.surname())
                .cpf(userDTO.cpf())
                .password(userDTO.password())
                .build();
    }
}
