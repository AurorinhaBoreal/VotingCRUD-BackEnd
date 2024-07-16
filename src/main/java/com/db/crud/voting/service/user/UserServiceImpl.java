package com.db.crud.voting.service.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.UserMapper;
import com.db.crud.voting.dto.request.UserLoginRequest;
import com.db.crud.voting.dto.request.UserRegisterRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse login(UserLoginRequest userLoginDto) {
        User user = userRepository.findByCpf(userLoginDto.cpf()).orElseThrow(() -> new RuntimeException("Cannot Find this User"));
        if (!user.getPassword().equals(userLoginDto.password())) {
            throw new RuntimeException("Password are divergent");
        }
        return UserMapper.userToDto(user);
    }

    public UserResponse register(UserRegisterRequest userRegisterDto) {
        Optional<User> user = userRepository.findByCpf(userRegisterDto.cpf());
        if (user.isPresent()) {
            throw new RuntimeException("This person already exists!");
        }
        User userRegistered = UserMapper.dtoToUser(userRegisterDto);
        userRepository.save(userRegistered);
        return UserMapper.userToDto(userRegistered);
    }
}
