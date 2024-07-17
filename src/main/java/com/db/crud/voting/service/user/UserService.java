package com.db.crud.voting.service.user;

import com.db.crud.voting.dto.request.UserRegisterRequest;
import com.db.crud.voting.dto.response.UserResponse;

public interface UserService {

    public UserResponse getUser(String cpf);

    public UserResponse register(UserRegisterRequest userRegisterRequest);
}
