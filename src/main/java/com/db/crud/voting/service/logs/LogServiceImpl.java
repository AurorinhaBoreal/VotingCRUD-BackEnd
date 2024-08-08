package com.db.crud.voting.service.logs;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.LogMapper;
import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.exception.InvalidEnumException;
import com.db.crud.voting.model.Log;
import com.db.crud.voting.repository.LogRepository;

@Service
public class LogServiceImpl implements LogService {
    
    LogRepository logRepository;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public List<LogResponse> getLogs() {
        List<LogResponse> logResponse = new ArrayList<>();
        List<Log> logs = logRepository.findAll();

        logs.forEach(log ->
            logResponse.add(LogMapper.logToDto(log))
        );

        return logResponse;
    }

    @Override
    public boolean addLog(LogObj logDto) {
        Operation operation = convertOperation(logDto.operation());
        LocalDateTime realizedOn = logDto.realizedOn().truncatedTo(ChronoUnit.SECONDS);

        Log log = LogMapper.infoToLog(logDto, operation, realizedOn);
        logRepository.save(log);
        
        return true;
    }

    private Operation convertOperation(String operation) {
        switch (operation) {
            case "C":
                return Operation.CREATE;            
            case "V":
                return Operation.VOTE;
            default:
                throw new InvalidEnumException("This is a Invalid Operation!");
        }
    }
}
