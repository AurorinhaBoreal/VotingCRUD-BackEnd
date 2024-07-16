package com.db.crud.voting.dto.mapper;

import org.mapstruct.Mapper;
import java.time.LocalDateTime;

import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.model.Log;

@Mapper(componentModel = "spring")
public interface LogMapper {
    
    static LogResponse logToDto(Log log) {
        return LogResponse.builder()
            .objectType(log.getObjectType())
            .objectInfo(log.getObjectInfo())
            .objectId(log.getObjectId())
            .operation(log.getOperation())
            .realizedOn(log.getRealizedOn())
            .build();
    }

    static Log infoToLog(String objectType, Long objectId, String objectInfo, Operation operation, LocalDateTime realizedOn) {
        return Log.builder()
            .objectType(objectType)
            .objectId(objectId)
            .objectInfo(objectInfo)
            .operation(operation)
            .realizedOn(realizedOn)
            .build();
    }
}
