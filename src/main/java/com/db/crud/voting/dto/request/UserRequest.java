package com.db.crud.voting.dto.request;

import org.hibernate.validator.constraints.br.CPF;

import com.db.crud.voting.enums.UserType;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRequest(
        @NotBlank(message = "You need to select a UserType")
        UserType userType,

        @NotBlank(message = "Must inform a first name")
        String firstName,

        @NotBlank(message = "Must inform a surname")
        String surname,

        @NotBlank(message = "Must informa a CPF")
        @CPF(message = "Invalid CPF")
        String cpf) {
}
