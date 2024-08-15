package com.db.crud.voting.unitary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.fixture.UserFixture;
import com.db.crud.voting.mapper.LogMapper;
import com.db.crud.voting.mapper.UserMapper;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.LogService;
import com.db.crud.voting.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitaryTests {
    
    @Mock
    UserRepository userRepository;
    
    @Mock
    UserMapper userMapper;

    @Mock
    LogMapper logMapper;

    @Mock
    LogService logService;

    @InjectMocks
    UserServiceImpl userService;

    UserRequest userDTOValid = UserFixture.UserDTOValid();
    User userEntityValid = UserFixture.UserEntityValid();
    UserResponse userResponseValid = UserFixture.UserResponseValid();
  
    @Test
    @DisplayName("Happy Test: Create User")
    void shouldCreateUser() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(userMapper.dtoToUser(userDTOValid)).thenReturn(userEntityValid);
        when(userMapper.userToDto(userEntityValid)).thenReturn(userResponseValid);
        userEntityValid.setId(1L);

        UserResponse userResponse = userService.register(userDTOValid);
        System.out.println(userResponse);
        assertNotNull(userResponse);
        assertEquals("Aurora", userResponse.firstName() );
    }

    @Test
    @DisplayName("Happy Test: Get User")
    void shouldGetUser() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(userEntityValid));
        when(userMapper.userToDto(userEntityValid)).thenReturn(userResponseValid);

        UserResponse user = userService.getUser("05073122011");

        assertNotNull(user);
    }

    @Test
    @DisplayName("Sad Test: Should thrown EntityExistsException")
    void thrownEntityExistsException() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(userEntityValid));

        EntityExistsException thrown = assertThrows(EntityExistsException.class, () -> {        
            userService.register(userDTOValid);
        });
    
        assertEquals("A person with this cpf already exists!", thrown.getMessage());
    }
}
