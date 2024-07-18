package com.db.crud.voting.fixture;

import com.db.crud.voting.dto.mapper.UserMapper;
import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.enums.converters.UserTypeConverter;
import com.db.crud.voting.model.User;

public class UserFixture {
    
    static UserTypeConverter userTypeConverter = new UserTypeConverter();

    public static UserRequest UserDTOValid() {
        return UserRequest.builder()
            .firstName("Aurora")
            .surname("Kruschewsky")
            .userType("A")
            .cpf("05073122011")
            .build();
    }

    public static User UserEntityValid() {
        UserType userType = userTypeConverter.convertToEntityAttribute("A");
        return UserMapper.dtoToUser(UserDTOValid(), userType);
    }
}
