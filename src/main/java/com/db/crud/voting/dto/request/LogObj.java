package com.db.crud.voting.dto.request;

import java.time.LocalDateTime;

import com.db.crud.voting.enums.Operation;

import lombok.Builder;

@Builder
public record LogObj(
    String objType,
    Long objId,
    String objInfo,
    Operation operation,
    LocalDateTime realizedOn) {
    
}
