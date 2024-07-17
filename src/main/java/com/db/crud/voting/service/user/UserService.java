package com.db.crud.voting.service.user;

import com.db.crud.voting.dto.request.UserLoginRequest;
import com.db.crud.voting.dto.request.UserRegisterRequest;
import com.db.crud.voting.dto.response.UserResponse;

public interface UserService {

    public UserResponse getUser(String cpf);
    
    public UserResponse login(UserLoginRequest userLoginRequest);

    public UserResponse register(UserRegisterRequest userRegisterRequest);
}
