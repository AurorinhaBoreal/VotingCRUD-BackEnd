package com.db.crud.voting.service.agenda;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.AgendaMapper;
import com.db.crud.voting.dto.mapper.AgendaMapperWrapper;
import com.db.crud.voting.dto.mapper.LogMapper;
import com.db.crud.voting.dto.mapper.VoteMapper;
import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.exception.AgendaEndedException;
import com.db.crud.voting.exception.AuthorizationException;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.exception.UserAlreadyVotedException;
import com.db.crud.voting.exception.VoteConflictException;
import com.db.crud.voting.model.Agenda;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.AgendaRepository;
import com.db.crud.voting.repository.UserRepository;
import com.db.crud.voting.service.logs.LogService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AgendaServiceImpl implements AgendaService {
    
    AgendaRepository agendaRepository;
    UserRepository userRepository;
    LogService logService;
    AgendaMapperWrapper agendaMapperWrapper;
    
    @Override
    public String finishAgenda() {
        agendaRepository.findByHasEnded(false).forEach(agenda -> {
            LocalDateTime actualDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            if (actualDate.isAfter(agenda.getFinishOn())) {
                agendaRepository.finishAgenda(agenda.getId());
                agendaRepository.save(agenda);
            }
        });
        return "Agenda Ended!";
    }

    @Override
    public List<AgendaResponse> getEndedAgendas() {
        return agendaRepository.findByHasEnded(true)
            .stream().map((agenda) -> AgendaMapper.agendaToDto(agenda)).toList();
    }

    @Override
    public List<AgendaResponse> getActiveAgendas() {
        return agendaRepository.findByHasEnded(false)
            .stream().map((agenda) -> AgendaMapper.agendaToDto(agenda)).toList();
    }

    @Override
    public AgendaResponse createAgenda(AgendaRequest agendaRequest) {
        User user = findUser(agendaRequest.cpf());

        authenticateUserAdmin(user.getUserType());
        Optional<Agenda> agenda = agendaRepository.findByQuestion(agendaRequest.question());

        verifyAgendaPresent(agenda);

        LocalDateTime agendaFinish = getFinishDate(agendaRequest.duration());
        Agenda agendaCreated = agendaMapperWrapper.dtoToAgenda(agendaRequest, agendaFinish);
        agendaRepository.save(agendaCreated);

        LogObj logObj = buildObj("Agenda", agendaCreated.getId(), agendaCreated.getQuestion(), Operation.CREATE, agendaCreated.getCreatedOn());
        logService.addLog(logObj);

        return AgendaMapper.agendaToDto(agendaCreated);
    }

    private void authenticateUserAdmin(UserType userType) {
        if (userType != UserType.ADMIN) {
            throw new AuthorizationException("You don't have authorization to create a agenda!");
        }
    }

    private void verifyAgendaPresent(Optional<Agenda> agenda) {
        if (agenda.isPresent()) {
            throw new EntityExistsException("This agenda was already created!");
        }
    }

    @Override
    public AddVoteResponse addVote(AddVoteRequest addvote) {
        User user = findUser(addvote.cpf());
        Agenda agenda = findAgenda(addvote.question());
        
        veifyAgendaFinished(agenda);

        List<User> usersVoted = agenda.getUsersVoted();

        verifyAndAddUserVoted(user, usersVoted);

        sortVote(addvote.vote(), agenda);

        agenda.setTotalVotes(agenda.getNoVotes()+agenda.getYesVotes());
        agendaRepository.save(agenda);
        
        LogObj logObj = buildObj("User", user.getId(), user.getFullname(), Operation.VOTE, LocalDateTime.now());
        logService.addLog(logObj);
        
        return VoteMapper.voteToDto(true, user.getCpf());
    }

    private void veifyAgendaFinished(Agenda agenda) {
        if (agenda.isHasEnded()) {
            throw new AgendaEndedException("This agenda already ended!");
        }
    }

    private void verifyAndAddUserVoted(User user, List<User> usersVoted) {
        if (usersVoted.contains(user)) {
            throw new UserAlreadyVotedException("This user already voted!");
        }
        usersVoted.add(user);
    }

    private void sortVote(String vote, Agenda agenda) {
        if (vote.equals("Y")) {
            int yVotes = agenda.getYesVotes();
            agenda.setYesVotes(yVotes+1);
        } else if (vote.equals("N")) {
            int nVotes = agenda.getNoVotes();
            agenda.setNoVotes(nVotes+1);
        } else {
            throw new VoteConflictException("Unknown Vote, invalidating Vote!");
        }
    }

    private Agenda findAgenda(String question) {
        return agendaRepository.findByQuestion(question).orElseThrow(
            () -> new CannotFindEntityException("Cannot find agenda with question: "+question)
        );
    }

    private User findUser(String cpf) {
        return userRepository.findByCpf(cpf).orElseThrow(
            () -> new CannotFindEntityException("The user with cpf: "+cpf+" isn't registered!")
        );
    }

    private LocalDateTime getFinishDate(Integer duration) {
        return (LocalDateTime.now().plusMinutes(duration)).truncatedTo(ChronoUnit.SECONDS);
    }

    private LogObj buildObj(String type, Long id, String question, Operation operation, LocalDateTime realizedOn) {
        return LogMapper.logObj(type, id, question, operation, realizedOn);
    }
}
