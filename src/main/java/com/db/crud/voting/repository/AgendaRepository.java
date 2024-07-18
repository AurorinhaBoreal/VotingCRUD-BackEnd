package com.db.crud.voting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.db.crud.voting.model.Agenda;

import jakarta.transaction.Transactional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    
    Optional<Agenda> findByQuestion(String question);
    List<Agenda> findByHasEnded(boolean hasEnded);

    @Transactional
    @Modifying
    @Query("update tbl_agenda a set a.hasEnded = true where a.id = :id_agenda")
    void finishAgenda(@Param(value = "id_agenda") Long id);
}
