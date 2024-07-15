package com.db.crud.voting.dto.response;

import com.db.crud.voting.enums.UserType;

import lombok.Builder;

@Builder
public record UserResponse(
        UserType userType,
        String firstName,
        String surname) {
}
