package com.db.crud.voting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.crud.voting.model.Agenda;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    
    Optional<Agenda> findByQuestion(String question);
    List<Agenda> findByHasEnded(boolean hasEnded);
}
