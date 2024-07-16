package com.db.crud.voting.service.logs;

import java.util.List;
import java.time.LocalDateTime;

import com.db.crud.voting.dto.response.LogResponse;

public interface LogService {
    
    public List<LogResponse> getLogs();

    public boolean addLog(
        String objectType, 
        Long objectId, 
        String objectInfo, 
        String operation, 
        LocalDateTime realizedOn);
}
