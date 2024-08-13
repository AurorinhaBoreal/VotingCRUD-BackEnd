package com.db.crud.voting.unitary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.db.crud.voting.dto.mapper.LogMapper;
import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.fixture.LogFixture;
import com.db.crud.voting.model.Log;
import com.db.crud.voting.repository.LogRepository;
import com.db.crud.voting.service.logs.LogServiceImpl;

@ExtendWith(MockitoExtension.class)
class LogServiceUnitaryTests {
    
    @Mock
    LogRepository logRepository;

    @Mock
    LogMapper logMapper;

    @InjectMocks
    LogServiceImpl logService;

    Log logEntityValid1 = LogFixture.LogEntityValid1();
    Log logEntityValid2 = LogFixture.LogEntityValid2();
    LogObj logObjValid = LogFixture.LogObjEntityValid1();

    @Test
    @DisplayName("Happy Test: Should Get Logs")
    void shouldGetLogs() {
        when(logRepository.findAll()).thenReturn(List.of(logEntityValid1));

        List<LogResponse> logs = logService.getLogs();

        assertTrue(logs.size() > 0);
    }

    @Test
    @DisplayName("Happy Test: Should add Log")
    void shouldAddLog() {
        boolean added = logService.addLog(logObjValid);

        assertTrue(added);
    }
}
