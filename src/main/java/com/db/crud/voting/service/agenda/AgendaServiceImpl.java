package com.db.crud.voting.service.agenda;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.AgendaMapper;
import com.db.crud.voting.dto.mapper.VoteMapper;
import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.dto.response.AgendaResponse;
import com.db.crud.voting.model.Agenda;
import com.db.crud.voting.model.User;
import com.db.crud.voting.repository.AgendaRepository;
import com.db.crud.voting.repository.UserRepository;

@Service
public class AgendaServiceImpl implements AgendaService {
    
    AgendaRepository agendaRepository;
    UserRepository userRepository;

    public AgendaServiceImpl(AgendaRepository agendaRepository, UserRepository userRepository) {
        this.agendaRepository = agendaRepository;
        this.userRepository = userRepository;
    }

    public List<Agenda> getEndedAgendas() {
        return agendaRepository.findByHasEnded(false);
    }

    public List<Agenda> getActiveAgendas() {
        return agendaRepository.findByHasEnded(true);
    }

    public AgendaResponse createAgenda(AgendaRequest agendaRequest) {
        Optional<Agenda> agenda = agendaRepository.findByQuestion(agendaRequest.question());
        if (agenda.isPresent()) {
            throw new RuntimeException("This agenda was already created!");
        }
        Agenda agendaCreated = AgendaMapper.dtoToAgenda(agendaRequest);
        agendaRepository.save(agendaCreated);
        return AgendaMapper.agendaToDto(agendaCreated);
    }

    public AddVoteResponse addVote(AddVoteRequest addvote) {
        Agenda agenda = agendaRepository.findByQuestion(addvote.question()).orElseThrow(() -> new RuntimeException("Cannot Find this Agenda"));
        User user = userRepository.findByCpf(addvote.userCpf()).orElseThrow(() -> new RuntimeException("Cannot find User"));
        List<User> usersVoted = agenda.getUsersVoted();
        if (usersVoted.contains(user)) {
            return VoteMapper.voteToDto(false, user.getCpf());
        }
        usersVoted.add(user);
        if (addvote.yes()) {
            int yVotes = agenda.getYesVotes();
            agenda.setYesVotes(yVotes+1);
        } else {
            int nVotes = agenda.getNoVotes();
            agenda.setNoVotes(nVotes+1);
        }
        agendaRepository.save(agenda);
        return VoteMapper.voteToDto(true, user.getCpf());
    }
}
