package com.db.crud.voting.service.impl;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.enums.UserType;
import com.db.crud.voting.enums.Vote;
import com.db.crud.voting.exception.AgendaEndedException;
import com.db.crud.voting.exception.AuthorizationException;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.exception.UserAlreadyVotedException;
import com.db.crud.voting.exception.VoteConflictException;
import com.db.crud.voting.mapper.AgendaMapper;
import com.db.crud.voting.mapper.LogMapper;
import com.db.crud.voting.mapper.VoteMapper;
import com.db.crud.voting.model.Agenda;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.AgendaRepository;
import com.db.crud.voting.service.AgendaService;
import com.db.crud.voting.service.LogService;
import com.db.crud.voting.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Configuration
@AllArgsConstructor
@EnableScheduling
public class AgendaServiceImpl implements AgendaService {
    
    AgendaRepository agendaRepository;
    UserService userService;
    LogService logService;
    AgendaMapper agendaMapper;
    VoteMapper voteMapper;
    LogMapper logMapper;
    
    @Scheduled(fixedDelay = 120000)
    public void finishAgenda() {
        agendaRepository.findByHasEnded(false).forEach(agenda -> {
            log.debug("Verify if any agendas has finished...");
            LocalDateTime actualDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            if (actualDate.isAfter(agenda.getFinishOn())) {
                log.info("Agenda Finished: "+agenda);
                agendaRepository.finishAgenda(agenda.getId());
                agendaRepository.save(agenda);
            }
        });
    }

    @Override
    public List<AgendaResponse> getEndedAgendas() {
        log.info("Requested Get Ended Agendas!");
        return agendaRepository.findByHasEnded(true)
            .stream().map(agenda -> agendaMapper.agendaToDto(agenda)).toList();
    }

    @Override
    public List<AgendaResponse> getActiveAgendas() {
        log.info("Requested Get Active Agendas!");
        return agendaRepository.findByHasEnded(false)
            .stream().map(agenda -> agendaMapper.agendaToDto(agenda)).toList();
    }

    @Override
    public AgendaResponse createAgenda(AgendaRequest agendaRequest) {
        log.info("Requested Create Agenda: ", agendaRequest);
        User user = findUser(agendaRequest.cpf());
        authenticateUserAdmin(user);
        log.debug("Admin User Authenticated");

        Optional<Agenda> agenda = agendaRepository.findByQuestion(agendaRequest.question());
        verifyAgendaPresent(agenda);
        log.debug("Agenda Doesn't Exist!");

        LocalDateTime agendaFinish = getFinishDate(agendaRequest.duration());
        Agenda agendaCreated = agendaMapper.dtoToAgenda(agendaRequest, agendaFinish);
        agendaRepository.save(agendaCreated);
        log.debug("Agenda created!");

        LogObj logObj = buildObj("Agenda", agendaCreated.getId(), agendaCreated.getQuestion(), Operation.CREATE, agendaCreated.getCreatedOn());
        logService.addLog(logObj);
        log.debug("Log Entity Created!");

        return agendaMapper.agendaToDto(agendaCreated);
    }

    private void authenticateUserAdmin(User user) {
        if (user.getUserType() != UserType.ADMIN) {
            log.error("User not Authenticated: "+user);
            throw new AuthorizationException("You don't have authorization to create a agenda!");
        }
    }

    private User findUser(String cpf) {
        return userService.getUser(cpf);
    }

    private void verifyAgendaPresent(Optional<Agenda> agenda) {
        if (agenda.isPresent()) {
            log.error("Agenda with the same question was already created!");
            throw new EntityExistsException("This agenda was already created!");
        }
    }

    @Override
    public AddVoteResponse addVote(AddVoteRequest addvote) {
        log.info("Requested Vote!");
        Agenda agenda = findAgenda(addvote.question());
        verifyAgendaFinished(agenda);
        log.debug("Agenda found!");

        User user = findUser(addvote.cpf());
        List<User> usersVoted = agenda.getUsersVoted();
        log.debug("User didn't vote in this agenda yet!");

        verifyUserVoted(user, usersVoted);
        usersVoted.add(user);

        sortVote(addvote.vote(), agenda);
        log.debug("Vote Sorted!");

        agenda.setTotalVotes(agenda.getNoVotes()+agenda.getYesVotes());
        agendaRepository.save(agenda);
        log.debug("Vote Contabilized!");
        
        LogObj logObj = buildObj("User", user.getId(), user.getFullname(), Operation.VOTE, LocalDateTime.now());
        logService.addLog(logObj);
        log.debug("Log Entity Created!");
        
        return voteMapper.voteToDto(user.getCpf());
    }

    private void verifyAgendaFinished(Agenda agenda) {
        if (agenda.isHasEnded()) {
            log.error("The following agendas has already ended: ", agenda);
            throw new AgendaEndedException("This agenda already ended!");
        }
    }

    private void verifyUserVoted(User user, List<User> usersVoted) {
        if (usersVoted.contains(user)) {
            log.error("This user has already voted on this agenda:", user);
            throw new UserAlreadyVotedException("This user already voted!");
        }
    }

    private void sortVote(Vote vote, Agenda agenda) {
        if (vote == Vote.YES) {
            int yVotes = agenda.getYesVotes();
            agenda.setYesVotes(yVotes+1);
        } else if (vote == Vote.NO) {
            int nVotes = agenda.getNoVotes();
            agenda.setNoVotes(nVotes+1);
        } else {
            log.info("Invalid Vote!");
            throw new VoteConflictException("Unknown Vote, invalidating Vote!");
        }
        log.info("Vote Added!");
    }

    private Agenda findAgenda(String question) {
        return agendaRepository.findByQuestion(question).orElseThrow(
            () -> new CannotFindEntityException("Cannot find agenda with question: "+question)
        );
    }

    private LocalDateTime getFinishDate(Integer duration) {
        return (LocalDateTime.now().plusMinutes(duration)).truncatedTo(ChronoUnit.SECONDS);
    }

    private LogObj buildObj(String type, Long id, String question, Operation operation, LocalDateTime realizedOn) {
        return logMapper.logObj(type, id, question, operation, realizedOn);
    }
}
