package com.db.crud.voting.service.user;

import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.LogMapper;
import com.db.crud.voting.dto.mapper.UserMapper;
import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.logs.LogService;

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
        Optional<User> user = userRepository.findByCpf(userRegisterDto.cpf());
        verifyUserPresent(user);
        log.debug("User didn't already exists!");

        User userRegistered = userMapper.dtoToUser(userRegisterDto);
        userRepository.save(userRegistered);
        log.debug("User Created!");

        LogObj logObj = buildObj("User", userRegistered.getId(), userRegistered.getFullname(), Operation.CREATE, userRegistered.getCreatedOn());
        logService.addLog(logObj);
        log.debug("Log Entity Created!");
        
        return userMapper.userToDto(userRegistered);
    }

    private void verifyUserPresent(Optional<User> user) {
        if (user.isPresent()) {
            log.error("User with this cpf already exists: ", user);
            throw new EntityExistsException("A person with this cpf already exists!");
        }
    }

    @Override
    public UserResponse getUser(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() -> new CannotFindEntityException("Cannot find this User"));
        return userMapper.userToDto(user);
    }

    private LogObj buildObj(String type, Long id, String name, Operation operation, LocalDateTime realizedOn) {
        return LogMapper.logObj(type, id, name, operation, realizedOn);
    }
}
