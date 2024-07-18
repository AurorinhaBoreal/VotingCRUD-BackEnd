package com.db.crud.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.crud.voting.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    
}
