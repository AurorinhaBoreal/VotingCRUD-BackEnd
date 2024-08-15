package com.db.crud.voting.fixture;

import com.db.crud.voting.mapper.UserMapperImpl;
import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.mapper.UserMapper;
import com.db.crud.voting.model.User;

public class UserFixture {

    public static UserRequest UserDTOValid() {
        return UserRequest.builder()
            .firstName("Aurora")
            .surname("Kruschewsky")
            .userType(UserType.ADMIN)
            .cpf("05073122011")
            .build();
    }

    public static User UserEntityValid() {
        UserMapper userMapper = new UserMapperImpl();
        return userMapper.dtoToUser(UserDTOValid());
    }

    public static UserResponse UserResponseValid() {
        UserMapper userMapper = new UserMapperImpl();
        return userMapper.userToDto(UserEntityValid());
    }
}
