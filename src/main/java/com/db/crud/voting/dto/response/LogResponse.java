package com.db.crud.voting.dto.response;

import java.time.LocalDateTime;

import com.db.crud.voting.enums.Operation;

import lombok.Builder;

@Builder
public record LogResponse(
    String objectType,
    Long objectId,
    String objectInfo,
    Operation operation,
    LocalDateTime realizedOn) {
}
