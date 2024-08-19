package com.db.crud.voting.service.impl;

import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.mapper.LogMapper;
import com.db.crud.voting.mapper.UserMapper;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.LogService;
import com.db.crud.voting.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    
    UserRepository userRepository;
    LogService logService;
    UserMapper userMapper;
    LogMapper logMapper;

    @Override
    public UserResponse register(UserRequest userRegisterDto) {
        log.debug("Requested Create User with Name: ", userRegisterDto.firstName());
        Optional<User> user = userRepository.findByCpf(userRegisterDto.cpf());
        verifyUserPresent(user);

        User userRegistered = userMapper.dtoToUser(userRegisterDto);
        userRepository.save(userRegistered);
        log.info("User Created!");

        LogObj logObj = buildObj("User", userRegistered.getId(), userRegistered.getFullname(), Operation.CREATE, userRegistered.getCreatedOn());
        logService.addLog(logObj);
        log.info("Log Entity Created!");
        
        return userMapper.userToDto(userRegistered);
    }

    private void verifyUserPresent(Optional<User> user) {
        if (user.isPresent()) {
            throw new EntityExistsException("A person with this cpf already exists!");
        }
    }

    @Override
    public UserResponse getUserResponse(String cpf) {
        log.info("Requested Specific User!");
        User user = userRepository.findByCpf(cpf).orElseThrow(() -> new CannotFindEntityException("Cannot find this User"));
        return userMapper.userToDto(user);
    }

    @Override
    public User getUser(String cpf) {
        return userRepository.findByCpf(cpf).orElseThrow(
            () -> new CannotFindEntityException("The user with cpf: "+cpf+" isn't registered!")
        );
    }

    private LogObj buildObj(String type, Long id, String name, Operation operation, LocalDateTime realizedOn) {
        return logMapper.logObj(type, id, name, operation, realizedOn);
    }
}
