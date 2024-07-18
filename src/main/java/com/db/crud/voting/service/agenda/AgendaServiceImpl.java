package com.db.crud.voting.service.agenda;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.AgendaMapper;
import com.db.crud.voting.dto.mapper.AgendaMapperWrapper;
import com.db.crud.voting.dto.mapper.VoteMapper;
import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.enums.Category;
import com.db.crud.voting.enums.converters.CategoryConverter;
import com.db.crud.voting.enums.converters.UserTypeConverter;
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

@Service
public class AgendaServiceImpl implements AgendaService {
    
    AgendaRepository agendaRepository;
    UserRepository userRepository;
    LogService logService;
    UserTypeConverter userTypeConverter;
    CategoryConverter categoryConverter;
    AgendaMapperWrapper agendaMapperWrapper;

    public AgendaServiceImpl(
            AgendaRepository agendaRepository, 
            UserRepository userRepository, 
            LogService logService, 
            CategoryConverter categoryConverter, 
            UserTypeConverter userTypeConverter,
            AgendaMapperWrapper agendaMapperWrapper
        ) {
        this.agendaRepository = agendaRepository;
        this.logService = logService;
        this.userRepository = userRepository;
        this.categoryConverter = categoryConverter;
        this.userTypeConverter = userTypeConverter;
        this.agendaMapperWrapper = agendaMapperWrapper;
    }

    public List<AgendaResponse> getEndedAgendas() {
        List<AgendaResponse> agendaResponse = new ArrayList<>();
        List<Agenda> agendas = agendaRepository.findByHasEnded(false);

        agendas.forEach(agenda -> 
            agendaResponse.add(AgendaMapper.agendaToDto(agenda))
        );

        return agendaResponse;
    }

    public List<AgendaResponse> getActiveAgendas() {
        List<AgendaResponse> agendaResponse = new ArrayList<>();
        List<Agenda> agendas = agendaRepository.findByHasEnded(true);

        agendas.forEach(agenda -> 
            agendaResponse.add(AgendaMapper.agendaToDto(agenda))
        );
        
        return agendaResponse;
    }

    public AgendaResponse createAgenda(AgendaRequest agendaRequest) {
        User user = findUser(agendaRequest.cpf());

        if (!userTypeConverter.convertToDatabaseColumn(user.getUserType()).equals("A")) {
            throw new AuthorizationException("You don't have authorization to create a agenda!");
        }

        Optional<Agenda> agenda = agendaRepository.findByQuestion(agendaRequest.question());
        if (agenda.isPresent()) {
            throw new EntityExistsException("This agenda was already created!");
        }

        Category agendaCategory = (categoryConverter.convertToEntityAttribute(agendaRequest.category()));
        Agenda agendaCreated = agendaMapperWrapper.dtoToAgenda(agendaRequest, agendaCategory);
        agendaRepository.save(agendaCreated);

        logService.addLog("Agenda", agendaCreated.getId(), agendaCreated.getQuestion(), "C", agendaCreated.getCreatedOn());

        return AgendaMapper.agendaToDto(agendaCreated);
    }

    public AddVoteResponse addVote(AddVoteRequest addvote) {
        User user = findUser(addvote.cpf());
        Agenda agenda = findAgenda(addvote.question());

        if (agenda.isHasEnded()) {
            throw new AgendaEndedException("This agenda already ended!");
        }
        List<User> usersVoted = agenda.getUsersVoted();
        if (usersVoted.contains(user)) {
            throw new UserAlreadyVotedException("This user already voted!");
        }
        usersVoted.add(user);

        if (addvote.yes() && addvote.no()) {
            throw new VoteConflictException("A 'yes' and 'no' Votes were contabilized, invalidating Vote!");
        } else if (addvote.no()) {
            int nVotes = agenda.getNoVotes();
            agenda.setNoVotes(nVotes+1);
        } else {
            int yVotes = agenda.getYesVotes();
            agenda.setYesVotes(yVotes+1);
        }

        agenda.setTotalVotes(agenda.getNoVotes()+agenda.getYesVotes());
        agendaRepository.save(agenda);
        
        logService.addLog("User", user.getId(), user.getFullname(), "V", LocalDateTime.now());
        
        return VoteMapper.voteToDto(true, user.getCpf());
    }

    public String finishAgenda(String question) {
        Agenda agenda = findAgenda(question);
        agendaRepository.finishAgenda(agenda.getId());
        agendaRepository.save(agenda);
        return "Agenda Ended!";
    }

    public Agenda findAgenda(String question) {
        return agendaRepository.findByQuestion(question).orElseThrow(
            () -> new CannotFindEntityException("Cannot find agenda with question: "+question)
        );
    }

    public User findUser(String cpf) {
        return userRepository.findByCpf(cpf).orElseThrow(
            () -> new CannotFindEntityException("The user with cpf: "+cpf+" isn't registered!")
        );
    }
}
