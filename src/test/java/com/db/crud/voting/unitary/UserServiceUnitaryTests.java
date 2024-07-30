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

import com.db.crud.voting.dto.mapper.UserMapperWrapper;
import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.dto.response.UserResponse;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.fixture.UserFixture;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.logs.LogService;
import com.db.crud.voting.service.user.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitaryTests {
    
    @Mock
    UserRepository userRepository;
    
    @Mock
    UserMapperWrapper userMapperWrapper;

    @Mock
    LogService logService;

    @InjectMocks
    UserServiceImpl userService;

    UserRequest userDTOValid = UserFixture.UserDTOValid();
    User userEntityValid = UserFixture.UserEntityValid();
  
    @Test
    @DisplayName("Happy Test: Create User")
    void shouldCreateUser() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(userService.userTypeConverter(anyString())).thenReturn(UserType.ADMIN);
        when(userMapperWrapper.dtoToUser(userDTOValid, UserType.ADMIN)).thenReturn(userEntityValid);
        userEntityValid.setId(1L);

        UserResponse userResponse = userService.register(userDTOValid);

        assertNotNull(userResponse);
        assertEquals("Aurora", userResponse.firstName() );
    }

    @Test
    @DisplayName("Happy Test: Get User")
    void shouldGetUser() {
        when(userRepository.findByCpf("05073122011")).thenReturn(Optional.of(userEntityValid));

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
