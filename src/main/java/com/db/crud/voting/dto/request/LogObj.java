package com.db.crud.voting.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record LogObj(
    String objType,
    Long objId,
    String objInfo,
    String operation,
    LocalDateTime realizedOn) {
    
}
