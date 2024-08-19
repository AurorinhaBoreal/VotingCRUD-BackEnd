package com.db.crud.voting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.dto.response.LogResponse;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.model.Log;

@Mapper(componentModel = "spring")
public interface LogMapper {
    
    LogResponse logToDto(Log log);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "logObj.objType", target = "objectType")
    @Mapping(source = "logObj.objId", target = "objectId")
    @Mapping(source = "logObj.objInfo", target = "objectInfo")
    @Mapping(source = "logObj.operation", target = "operation")
    @Mapping(source = "realizedOn", target = "realizedOn")
    Log infoToLog(LogObj logObj, LocalDateTime realizedOn);

    LogObj logObj(String objType, Long objId, String objInfo, Operation operation, LocalDateTime realizedOn);
}
