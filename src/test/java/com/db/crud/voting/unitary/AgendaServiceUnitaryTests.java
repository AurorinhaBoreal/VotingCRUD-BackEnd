package com.db.crud.voting.unitary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.exception.AuthorizationException;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.exception.UserAlreadyVotedException;
import com.db.crud.voting.fixture.AgendaFixture;
import com.db.crud.voting.fixture.LogFixture;
import com.db.crud.voting.fixture.UserFixture;
import com.db.crud.voting.fixture.VoteFixture;
import com.db.crud.voting.mapper.AgendaMapper;
import com.db.crud.voting.mapper.LogMapper;
import com.db.crud.voting.mapper.VoteMapper;
import com.db.crud.voting.model.Agenda;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.AgendaRepository;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.impl.AgendaServiceImpl;
import com.db.crud.voting.service.LogService;
import com.db.crud.voting.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class AgendaServiceUnitaryTests {

    @Mock
    AgendaMapper agendaMapper;

    @Mock
    AgendaRepository agendaRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserServiceImpl userService;

    @Mock
    LogService logService;

    @Mock
    LogMapper logMapper;

    @Mock
    VoteMapper voteMapper;

    @InjectMocks
    AgendaServiceImpl agendaService;

    AgendaRequest agendaDTOValid = AgendaFixture.AgendaDTOValid();
    Agenda agendaEntityValid = AgendaFixture.AgendaEntityValid();
    AgendaResponse agendaResponseValid = AgendaFixture.AgendaResponseValid();

    User userEntityValid = UserFixture.UserEntityValid();

    AddVoteRequest voteDTOValid1 = VoteFixture.AddVote1();
    AddVoteRequest voteDTOValid2 = VoteFixture.AddVote2();
    AddVoteResponse voteResponseValid = VoteFixture.AddVoteResponse();

    LogObj logObjValid = LogFixture.LogObjEntityValid1();

    @Test
    @DisplayName("Happy Test: Create Agenda")
    void shouldCreateAgenda() {
        LocalDateTime finishOn = (LocalDateTime.now()).truncatedTo(ChronoUnit.SECONDS).plusMinutes(agendaDTOValid.duration());
        
        agendaEntityValid.setId(2L);

        when(userService.getUser(anyString())).thenReturn(userEntityValid);
        when(agendaRepository.findByQuestion(anyString())).thenReturn(Optional.empty());
        when(agendaMapper.dtoToAgenda(agendaDTOValid, finishOn)).thenReturn(agendaEntityValid);
        when(agendaMapper.agendaToDto(agendaEntityValid)).thenReturn(agendaResponseValid);
        when(logMapper.logObj("Agenda", agendaEntityValid.getId(), agendaEntityValid.getQuestion(), Operation.CREATE, agendaEntityValid.getCreatedOn())).thenReturn(logObjValid);
        
        AgendaResponse agenda = agendaService.createAgenda(agendaDTOValid);

        assertNotNull(agenda);
    }

    @Test
    @DisplayName("Happy Test: Add Yes Vote")
    void shouldAddYesVote() {
        when(userService.getUser(anyString())).thenReturn(userEntityValid);
        when(agendaRepository.findByQuestion(anyString())).thenReturn(Optional.of(agendaEntityValid));
        when(voteMapper.voteToDto(userEntityValid.getCpf())).thenReturn(voteResponseValid);
    
        userEntityValid.setId(4L);
        agendaService.addVote(voteDTOValid1);

        assertTrue(agendaEntityValid.getTotalVotes() > 0);
    }

    @Test
    @DisplayName("Happy Test: Add No Vote")
    void shouldAddNoVote() {
        when(userService.getUser(anyString())).thenReturn(userEntityValid);
        when(agendaRepository.findByQuestion(anyString())).thenReturn(Optional.of(agendaEntityValid));
    
        userEntityValid.setId(4L);
        agendaService.addVote(voteDTOValid2);

        assertTrue(agendaEntityValid.getTotalVotes() > 0);
    }

    @Test
    @DisplayName("Happy Test: End Agenda")
    void shouldfinishAgenda() {
        agendaEntityValid.setId(1L);
        agendaEntityValid.setHasEnded(true);
        agendaService.finishAgenda();
        

        assertTrue(agendaEntityValid.isHasEnded());
    }

    @Test
    @DisplayName("Sad Test: Should thrown AuthorizationException")
    void thrownAuthorizationException() {
        userEntityValid.setUserType(UserType.COMMON);
        when(userService.getUser(anyString())).thenReturn(userEntityValid);
        AuthorizationException thrown = assertThrows(AuthorizationException.class, () -> {
            agendaService.createAgenda(agendaDTOValid);
        });
        
        assertEquals("You don't have authorization to create a agenda!", thrown.getMessage());
    }

    @Test
    @DisplayName("Sad Test: Should Thrown EntityExistsException")
    void thrownEntityExistsException() {
        when(userService.getUser(anyString())).thenReturn(userEntityValid);
        when(agendaRepository.findByQuestion(anyString())).thenReturn(Optional.of(agendaEntityValid));
        
    EntityExistsException thrown = assertThrows(EntityExistsException.class, () -> {
        agendaService.createAgenda(agendaDTOValid);
    });
    
    assertEquals("This agenda was already created!", thrown.getMessage());
    }

    @Test
    @DisplayName("Sad Test: Should Thrown UserALreadyVotedException")
    void thrownUserAlreadyVotedException() {
        when(userService.getUser(anyString())).thenReturn(userEntityValid);
        when(agendaRepository.findByQuestion(anyString())).thenReturn(Optional.of(agendaEntityValid));
    
        userEntityValid.setId(4L);
        agendaService.addVote(voteDTOValid2);
    UserAlreadyVotedException thrown = assertThrows(UserAlreadyVotedException.class, () -> {
        agendaService.addVote(voteDTOValid2);
    });
    
    assertEquals("This user already voted!", thrown.getMessage());
    }
}
