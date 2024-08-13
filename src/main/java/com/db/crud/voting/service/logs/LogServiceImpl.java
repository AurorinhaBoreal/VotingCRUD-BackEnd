package com.db.crud.voting.service.logs;

import java.util.List;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.db.crud.voting.dto.mapper.LogMapper;
import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.model.Log;
import com.db.crud.voting.repository.LogRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService {
    
    LogRepository logRepository;
    LogMapper logMapper;

    @Override
    public List<LogResponse> getLogs() {
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
}
