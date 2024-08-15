package com.db.crud.voting.service;

import java.util.List;

import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.LogResponse;

public interface LogService {
    
    List<LogResponse> getLogs();

    boolean addLog(
        LogObj logDto);
}
