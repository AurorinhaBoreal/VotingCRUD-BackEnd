package com.db.crud.voting.dto.mapper;

import org.mapstruct.Mapper;
import java.time.LocalDateTime;

import com.db.crud.voting.dto.request.LogObj;
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

    static Log infoToLog(LogObj logObj, Operation operation, LocalDateTime realizedOn) {
        return Log.builder()
            .objectType(logObj.objType())
            .objectId(logObj.objId())
            .objectInfo(logObj.objInfo())
            .operation(operation)
            .realizedOn(realizedOn)
            .build();
    }

    static LogObj logObj(String objectType, Long objectId, String objectInfo, String operation, LocalDateTime realizedOn) {
        return LogObj.builder()
            .objType(objectType)
            .objId(objectId)
            .objInfo(objectInfo)
            .operation(operation)
            .realizedOn(realizedOn)
            .build();
    }
}
