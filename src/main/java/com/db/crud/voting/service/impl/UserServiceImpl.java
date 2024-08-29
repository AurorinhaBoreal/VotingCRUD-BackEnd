package com.db.crud.voting.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.exception.AuthorizationException;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.exception.EntityExistsException;
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

    @Override
    public UserResponse register(UserRequest userRegisterDto) {
        log.debug("Requested Create User with Name: ", userRegisterDto.firstName());
        Optional<User> user = userRepository.findByCpf(userRegisterDto.cpf());
        verifyUserPresent(user);

        User userRegistered = userMapper.dtoToUser(userRegisterDto);
        userRepository.save(userRegistered);
        log.info("User Created!");

        LogObj logObj = logService.buildObj("User", userRegistered.getId(), userRegistered.getFullname(), Operation.CREATE, userRegistered.getCreatedOn());
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

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
            .stream().map(user -> userMapper.userToDto(user)).toList();
    }

    @Override
    public void authenticateUserAdmin(User user) {
        if (user.getUserType() != UserType.ADMIN) {
            throw new AuthorizationException("You don't have authorization to create a agenda!");
        }
    }

    @Override
    public void allowAccess(String cpf) {
        User user = getUser(cpf);
        if (user.getUserType() != UserType.ADMIN) {
            throw new AuthorizationException("The User isn't allowed to acess this information!");
        }
    }
}
