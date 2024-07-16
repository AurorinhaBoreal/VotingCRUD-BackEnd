package com.db.crud.voting.service.user;

import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.UserMapper;
import com.db.crud.voting.dto.request.UserLoginRequest;
import com.db.crud.voting.dto.request.UserRegisterRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.exception.AuthorizationException;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.logs.LogService;

import jakarta.persistence.EntityExistsException;

@Service
public class UserServiceImpl implements UserService {
    
    UserRepository userRepository;
    LogService logService;

    public UserServiceImpl(UserRepository userRepository, LogService logService) {
        this.userRepository = userRepository;
        this.logService = logService;
    }

    public UserResponse login(UserLoginRequest userLoginDto) {
        User user = userRepository.findByCpf(userLoginDto.cpf()).orElseThrow(() -> new CannotFindEntityException("Cannot Find this User"));
        if (!user.getPassword().equals(userLoginDto.password())) {
            throw new AuthorizationException("Password Incorrect");
        }
        logService.addLog("User", user.getId(), user.getFullname(), "L", LocalDateTime.now());
        return UserMapper.userToDto(user);
    }

    public UserResponse register(UserRegisterRequest userRegisterDto) {
        Optional<User> user = userRepository.findByCpf(userRegisterDto.cpf());
        if (user.isPresent()) {
            throw new EntityExistsException("A person with this cpf already exists!");
        }
        User userRegistered = UserMapper.dtoToUser(userRegisterDto);
        userRepository.save(userRegistered);
        logService.addLog("User", userRegistered.getId(), userRegistered.getFullname(), "C", userRegistered.getCreatedOn());
        return UserMapper.userToDto(userRegistered);
    }
}
