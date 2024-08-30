package com.db.crud.voting.service.impl;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.request.VoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.VoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.enums.Vote;
import com.db.crud.voting.exception.AgendaEndedException;
import com.db.crud.voting.exception.CannotFindEntityException;
import com.db.crud.voting.exception.EntityExistsException;
import com.db.crud.voting.exception.UserAlreadyVotedException;
import com.db.crud.voting.exception.VoteConflictException;
import com.db.crud.voting.mapper.AgendaMapper;
import com.db.crud.voting.mapper.VoteMapper;
import com.db.crud.voting.model.Agenda;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.AgendaRepository;
import com.db.crud.voting.service.AgendaService;
import com.db.crud.voting.service.LogService;
import com.db.crud.voting.service.UserService;

import jakarta.transaction.Transactional;
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
    
    @Transactional
    @Scheduled(fixedDelay = 120000)
    public void finishAgenda() {
        agendaRepository.findByHasEnded(false).forEach(agenda -> {
        LocalDateTime actualDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        if (actualDate.isAfter(agenda.getFinishOn())) {
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
        log.debug("Requested Create Agenda: ", agendaRequest);
        User user = findUser(agendaRequest.cpf());
        log.info("Admin User Authenticated");
        authenticateUserAdmin(user);
        Optional<Agenda> agenda = agendaRepository.findByQuestion(agendaRequest.question());
        verifyAgendaPresent(agenda);

        LocalDateTime agendaFinish = getFinishDate(agendaRequest.duration());
        Agenda agendaCreated = agendaMapper.dtoToAgenda(agendaRequest, agendaFinish);
        agendaRepository.save(agendaCreated);
        log.info("Agenda created!");

        LogObj logObj = logService.buildObj("Agenda", agendaCreated.getId(), agendaCreated.getQuestion(), Operation.CREATE, agendaCreated.getCreatedOn());
        logService.addLog(logObj);
        log.info("Log Entity Created!");

        return agendaMapper.agendaToDto(agendaCreated);
    }

    private void authenticateUserAdmin(User user) {
        userService.authenticateUserAdmin(user);
    }

    private User findUser(String cpf) {
        return userService.getUser(cpf);
    }

    private void verifyAgendaPresent(Optional<Agenda> agenda) {
        if (agenda.isPresent()) {
            throw new EntityExistsException("This agenda was already created!");
        }
    }

    @Override
    public VoteResponse addVote(VoteRequest addvote) {
        log.info("Requested Vote!");
        Agenda agenda = findAgenda(addvote.question());
        verifyAgendaFinished(agenda);
        log.info("Agenda found!");

        User user = findUser(addvote.cpf());
        List<User> usersVoted = agenda.getUsersVoted();

        verifyUserVoted(user, usersVoted);
        usersVoted.add(user);

        countVote(addvote.vote(), agenda);

        agenda.setTotalVotes(agenda.getNoVotes()+agenda.getYesVotes());
        agendaRepository.save(agenda);
        
        LogObj logObj = logService.buildObj("User", user.getId(), user.getFullname(), Operation.VOTE, LocalDateTime.now());
        logService.addLog(logObj);
        log.info("Log Entity Created!");
        
        return voteMapper.voteToDto(user.getCpf());
    }

    @Transactional
    @Override
    public void endAgendaEarly(String question) {
        question = question+"?";
        Agenda agenda = findAgenda(question);
        endAgenda(agenda);
    }

    @Transactional
    private void endAgenda(Agenda agenda) {
        agendaRepository.finishAgenda(agenda.getId());
    }

    @Override
    public void removeAgenda(String question) {
        question = question+"?";
        Agenda agenda = findAgenda(question);
        agendaRepository.delete(agenda);
    }

    private void verifyAgendaFinished(Agenda agenda) {
        if (agenda.isHasEnded()) {
            throw new AgendaEndedException("This agenda already ended!");
        }
    }

    private void verifyUserVoted(User user, List<User> usersVoted) {
        if (usersVoted.contains(user)) {
            throw new UserAlreadyVotedException("This user already voted!");
        }
    }

    private void countVote(Vote vote, Agenda agenda) {
        if (vote == Vote.YES) {
            int yVotes = agenda.getYesVotes();
            agenda.setYesVotes(yVotes+1);
        } else if (vote == Vote.NO) {
            int nVotes = agenda.getNoVotes();
            agenda.setNoVotes(nVotes+1);
        } else {
            throw new VoteConflictException("Unknown Vote, invalidating Vote!");
        }
        log.info("Vote Contabilized!");
    }

    private Agenda findAgenda(String question) {
        return agendaRepository.findByQuestion(question).orElseThrow(
            () -> new CannotFindEntityException("Cannot find agenda with question: "+question)
        );
    }

    private LocalDateTime getFinishDate(Integer duration) {
        return (LocalDateTime.now().plusMinutes(duration)).truncatedTo(ChronoUnit.SECONDS);
    }
}
