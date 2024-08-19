package com.db.crud.voting.service;

import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.model.User;

public interface UserService {

    UserResponse getUserResponse(String cpf);

    User getUser(String cpf);

    void authenticateUserAdmin(User user);

    UserResponse register(UserRequest userRegisterRequest);
}
