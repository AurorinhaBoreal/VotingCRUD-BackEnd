package com.db.crud.voting.service.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.UserMapper;
import com.db.crud.voting.dto.mapper.UserMapperWrapper;
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

    public UserResponse register(UserRequest userRegisterDto) {
        Optional<User> user = userRepository.findByCpf(userRegisterDto.cpf());
        if (user.isPresent()) {
            throw new EntityExistsException("A person with this cpf already exists!");
        }
        UserType userType = userTypeConverter(userRegisterDto.userType());
        User userRegistered = userMapperWrapper.dtoToUser(userRegisterDto, userType);
        userRepository.save(userRegistered);

        logService.addLog("User", userRegistered.getId(), userRegistered.getFullname(), "C", userRegistered.getCreatedOn());
        
        return UserMapper.userToDto(userRegistered);
    }

    public UserResponse getUser(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() -> new CannotFindEntityException("Cannot find this User"));
        return UserMapper.userToDto(user);
    }

    public UserType userTypeConverter(String userType) {
        switch (userType) {
            case "A":
                return UserType.ADMIN;
            case "C":
                return UserType.COMMON;
            default:
                throw new InvalidEnumException("This userType isn't Valid!");
        }
    }
}
