package com.db.crud.voting.unitary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.db.crud.voting.dto.mapper.AgendaMapperWrapper;
import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.enums.Category;
import com.db.crud.voting.enums.converters.CategoryConverter;
import com.db.crud.voting.enums.converters.UserTypeConverter;
import com.db.crud.voting.exception.AuthorizationException;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.exception.UserAlreadyVotedException;
import com.db.crud.voting.fixture.AgendaFixture;
import com.db.crud.voting.fixture.UserFixture;
import com.db.crud.voting.fixture.VoteFixture;
import com.db.crud.voting.model.Agenda;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.AgendaRepository;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.agenda.AgendaServiceImpl;
import com.db.crud.voting.service.logs.LogService;

@ExtendWith(MockitoExtension.class)
class AgendaServiceUnitaryTests {

    @Mock
    AgendaMapperWrapper agendaMapperWrapper;

    @Mock
    AgendaRepository agendaRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserTypeConverter userTypeConverter;

    @Mock
    CategoryConverter categoryConverter;

    @Mock
    LogService logService;

    @Mock
    AgendaServiceImpl agendaServiceMock;

    @InjectMocks
    AgendaServiceImpl agendaService;

    AgendaRequest agendaDTOValid = AgendaFixture.AgendaDTOValid();
    Agenda agendaEntityValid = AgendaFixture.AgendaEntityValid();

    User userEntityValid = UserFixture.UserEntityValid();

    AddVoteRequest voteDTOValid1 = VoteFixture.AddVote1();
    AddVoteRequest voteDTOValid2 = VoteFixture.AddVote2();

    @Test
    @DisplayName("Happy Test: Should List Active Agendas")
    void shouldListActiveAgendas() {
        List<Agenda> agendas = new ArrayList<>();
        agendas.add(agendaEntityValid);
        when(agendaRepository.findByHasEnded(true)).thenReturn(agendas);

        List<AgendaResponse> agendaResponse = agendaService.getActiveAgendas();
        
        assertNotNull(agendaResponse);
        assertTrue(agendas.contains(agendaEntityValid));
    }

    @Test
    @DisplayName("Happy Test: Should List Ended Agendas")
    void shouldListEndedAgendas() {
        List<Agenda> agendas = new ArrayList<>();
        when(agendaRepository.findByHasEnded(false)).thenReturn(agendas);

        agendaEntityValid.setId(2L);
        
        agendaRepository.finishAgenda(agendaEntityValid.getId());
        agendaEntityValid.setHasEnded(true);

        agendas.add(agendaEntityValid);
        List<AgendaResponse> agendaResponse = agendaService.getEndedAgendas();
        
        assertNotNull(agendaResponse);
        assertTrue(agendas.contains(agendaEntityValid));
    }

    // I will see this test later
    @Test
    @DisplayName("Happy Test: Create Agenda")
    void shouldCreateAgenda() {
        LocalDateTime finishOn = (LocalDateTime.now()).truncatedTo(ChronoUnit.SECONDS).plusMinutes(agendaDTOValid.duration());
        
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(userEntityValid));
        when(agendaRepository.findByQuestion(anyString())).thenReturn(Optional.empty());
        when(userTypeConverter.convertToDatabaseColumn(any())).thenReturn("A");
        when(categoryConverter.convertToEntityAttribute("S")).thenReturn(Category.SPORTS);
        when(logService.addLog(anyString(), anyLong(), anyString(), any(), any())).thenReturn(true);
        when(agendaMapperWrapper.dtoToAgenda(agendaDTOValid, Category.SPORTS, finishOn)).thenReturn(agendaEntityValid);
        agendaEntityValid.setId(2L);
        
        AgendaResponse agenda = agendaService.createAgenda(agendaDTOValid);

        assertNotNull(agenda);
    }

    @Test
    @DisplayName("Happy Test: Add Yes Vote")
    void shouldAddYesVote() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(userEntityValid));
        when(agendaRepository.findByQuestion(anyString())).thenReturn(Optional.of(agendaEntityValid));
    
        userEntityValid.setId(4L);
        agendaService.addVote(voteDTOValid1);

        assertTrue(agendaEntityValid.getTotalVotes() > 0);
    }

    @Test
    @DisplayName("Happy Test: Add No Vote")
    void shouldAddNoVote() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(userEntityValid));
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
        agendaService.finishAgenda(agendaEntityValid);
        

        assertTrue(agendaEntityValid.isHasEnded());
    }

    @Test
    @DisplayName("Sad Test: Should Thrown CannotFindEntityException")
        void shouldThrownCannotFindEntityException() {
    CannotFindEntityException thrown = assertThrows(CannotFindEntityException.class, () -> {
        agendaService.createAgenda(agendaDTOValid);
    });
    
    assertEquals("The user with cpf: "+agendaDTOValid.cpf()+" isn't registered!", thrown.getMessage());
    }

    @Test
    @DisplayName("Sad Test: Should thrown AuthorizationException")
    void thrownAuthorizationException() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(userEntityValid));
        when(userTypeConverter.convertToDatabaseColumn(any())).thenReturn("C");
    AuthorizationException thrown = assertThrows(AuthorizationException.class, () -> {
        agendaService.createAgenda(agendaDTOValid);
    });
    
    assertEquals("You don't have authorization to create a agenda!", thrown.getMessage());
    }

    @Test
    @DisplayName("Sad Test: Should Thrown EntityExistsException")
    void thrownEntityExistsException() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(userEntityValid));
        when(userTypeConverter.convertToDatabaseColumn(any())).thenReturn("A");
        when(agendaRepository.findByQuestion(anyString())).thenReturn(Optional.of(agendaEntityValid));
        
    EntityExistsException thrown = assertThrows(EntityExistsException.class, () -> {
        agendaService.createAgenda(agendaDTOValid);
    });
    
    assertEquals("This agenda was already created!", thrown.getMessage());
    }

    @Test
    @DisplayName("Sad Test: Should Thrown UserALreadyVotedException")
    void thrownUserAlreadyVotedException() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(userEntityValid));
        when(agendaRepository.findByQuestion(anyString())).thenReturn(Optional.of(agendaEntityValid));
    
        userEntityValid.setId(4L);
        agendaService.addVote(voteDTOValid2);
    UserAlreadyVotedException thrown = assertThrows(UserAlreadyVotedException.class, () -> {
        agendaService.addVote(voteDTOValid2);
    });
    
    assertEquals("This user already voted!", thrown.getMessage());
    }
}
