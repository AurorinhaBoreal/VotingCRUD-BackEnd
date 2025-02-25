package com.db.crud.voting.service.impl;

import java.util.List;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.mapper.LogMapper;
import com.db.crud.voting.model.Log;
import com.db.crud.voting.repository.LogRepository;
import com.db.crud.voting.service.LogService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService {
    
    LogRepository logRepository;
    LogMapper logMapper;

    @Override
    public List<LogResponse> getLogs() {
        log.info("Logs Requested!");
        return logRepository.findAll()
            .stream().map(log -> logMapper.logToDto(log)).toList();
    }

    @Override
    public boolean addLog(LogObj logDto) {
        LocalDateTime realizedOn = logDto.realizedOn().truncatedTo(ChronoUnit.SECONDS);

        Log log = logMapper.infoToLog(logDto, realizedOn);
        logRepository.save(log);
        
        return true;
    }

    @Override
    public LogObj buildObj(String type, Long id, String name, Operation operation, LocalDateTime realizedOn) {
        return logMapper.logObj(type, id, name, operation, realizedOn);
    }
}
