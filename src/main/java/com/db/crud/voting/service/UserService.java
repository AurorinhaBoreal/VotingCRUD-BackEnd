package com.db.crud.voting.service;

import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;

public interface UserService {

    UserResponse getUser(String cpf);

    UserResponse register(UserRequest userRegisterRequest);
}
