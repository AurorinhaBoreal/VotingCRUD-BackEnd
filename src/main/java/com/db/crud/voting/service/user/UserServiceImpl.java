package com.db.crud.voting.service.user;

import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.LogMapper;
import com.db.crud.voting.dto.mapper.UserMapper;
import com.db.crud.voting.dto.mapper.UserMapperWrapper;
import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.exception.InvalidEnumException;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.logs.LogService;

@Service
public class UserServiceImpl implements UserService {
    
    UserRepository userRepository;
    LogService logService;
    UserMapperWrapper userMapperWrapper;

    public UserServiceImpl(
        UserRepository userRepository, 
        LogService logService,
        UserMapperWrapper userMapperWrapper) {
        this.userRepository = userRepository;
        this.logService = logService;
        this.userMapperWrapper = userMapperWrapper;
    }

    @Override
    public UserResponse register(UserRequest userRegisterDto) {
        Optional<User> user = userRepository.findByCpf(userRegisterDto.cpf());
        if (user.isPresent()) {
            throw new EntityExistsException("A person with this cpf already exists!");
        }
        UserType userType = userTypeConverter(userRegisterDto.userType());
        User userRegistered = userMapperWrapper.dtoToUser(userRegisterDto, userType);
        userRepository.save(userRegistered);

        LogObj logObj = buildObj("User", userRegistered.getId(), userRegistered.getFullname(), "C", userRegistered.getCreatedOn());
        logService.addLog(logObj);
        
        return UserMapper.userToDto(userRegistered);
    }

    @Override
    public UserResponse getUser(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() -> new CannotFindEntityException("Cannot find this User"));
        return UserMapper.userToDto(user);
    }

    private UserType userTypeConverter(String userType) {
        switch (userType) {
            case "A":
                return UserType.ADMIN;
            case "C":
                return UserType.COMMON;
            default:
                throw new InvalidEnumException("This userType isn't Valid!");
        }
    }

    private LogObj buildObj(String type, Long id, String name, String operation, LocalDateTime realizedOn) {
        return LogMapper.logObj(type, id, name, operation, realizedOn);
    }
}
