package com.db.crud.voting.unitary;

import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.enums.converters.OperationConverter;
import com.db.crud.voting.fixture.LogFixture;
import com.db.crud.voting.model.Log;
import com.db.crud.voting.repository.LogRepository;
import com.db.crud.voting.service.logs.LogServiceImpl;

@ExtendWith(MockitoExtension.class)
class LogServiceUnitaryTests {
    
    @Mock
    LogRepository logRepository;

    @Mock
    OperationConverter operationConverter;

    @InjectMocks
    LogServiceImpl logService;

    Log logEntityValid1 = LogFixture.LogEntityValid1();
    Log logEntityValid2 = LogFixture.LogEntityValid2();

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
        when(operationConverter.convertToEntityAttribute("C")).thenReturn(Operation.CREATE);

        String objType = logEntityValid2.getObjectType();
        Long objId = logEntityValid2.getId();
        String objInfo = logEntityValid2.getObjectInfo();
        String operationCode = "C";

        boolean added = logService.addLog(objType, objId, objInfo, operationCode, LocalDateTime.now());

        assertTrue(added);
    }
}
