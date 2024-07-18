package com.db.crud.voting.service.logs;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.LogMapper;
import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.enums.converters.OperationConverter;
import com.db.crud.voting.model.Log;
import com.db.crud.voting.repository.LogRepository;

@Service
public class LogServiceImpl implements LogService {
    
    LogRepository logRepository;
    OperationConverter operationConverter;

    public LogServiceImpl(
        LogRepository logRepository, 
        OperationConverter operationConverter) {
        this.logRepository = logRepository;
        this.operationConverter = operationConverter;
    }

    public List<LogResponse> getLogs() {
        List<LogResponse> logResponse = new ArrayList<>();
        List<Log> logs = logRepository.findAll();

        logs.forEach(log ->
            logResponse.add(LogMapper.logToDto(log))
        );

        return logResponse;
    }

    public boolean addLog(String objectType, Long objectId, String objectInfo, String operationCode, LocalDateTime realizedOn) {
        Operation operationType = operationConverter.convertToEntityAttribute(operationCode);

        Log log = LogMapper.infoToLog(objectType, objectId, objectInfo, operationType, realizedOn);
        logRepository.save(log);
        
        return true;
    }
}
