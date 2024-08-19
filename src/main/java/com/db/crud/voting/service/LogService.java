package com.db.crud.voting.service;

import java.time.LocalDateTime;
import java.util.List;

import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.enums.Operation;

public interface LogService {
    
    List<LogResponse> getLogs();

    boolean addLog(
        LogObj logDto);

    LogObj buildObj(
        String type, 
        Long id, 
        String name, 
        Operation operation, 
        LocalDateTime realizedOn
        );
}
