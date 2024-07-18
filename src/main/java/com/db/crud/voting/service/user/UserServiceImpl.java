package com.db.crud.voting.service.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.UserMapper;
import com.db.crud.voting.dto.mapper.UserMapperWrapper;
import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.enums.converters.UserTypeConverter;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.logs.LogService;

@Service
public class UserServiceImpl implements UserService {
    
    UserRepository userRepository;
    LogService logService;
    UserTypeConverter userTypeConverter;
    UserMapperWrapper userMapperWrapper;

    public UserServiceImpl(
        UserRepository userRepository, 
        LogService logService, 
        UserTypeConverter userTypeConverter,
        UserMapperWrapper userMapperWrapper) {
        this.userRepository = userRepository;
        this.logService = logService;
        this.userTypeConverter = userTypeConverter;
        this.userMapperWrapper = userMapperWrapper;
    }

    public UserResponse register(UserRequest userRegisterDto) {
        Optional<User> user = userRepository.findByCpf(userRegisterDto.cpf());
        if (user.isPresent()) {
            throw new EntityExistsException("A person with this cpf already exists!");
        }
        UserType userType = userTypeConverter.convertToEntityAttribute(userRegisterDto.userType());
        User userRegistered = userMapperWrapper.dtoToUser(userRegisterDto, userType);
        userRepository.save(userRegistered);

        logService.addLog("User", userRegistered.getId(), userRegistered.getFullname(), "C", userRegistered.getCreatedOn());
        
        return UserMapper.userToDto(userRegistered);
    }

    public UserResponse getUser(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() -> new CannotFindEntityException("Cannot find this User"));
        return UserMapper.userToDto(user);
    }
}
