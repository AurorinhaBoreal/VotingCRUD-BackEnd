package com.db.crud.voting.dto.request;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
    @NotBlank(message = "Please insert a CPF") 
    @CPF(message = "Invalid CPF") 
    String cpf,

    @NotBlank(message = "Must inform a password") 
    String password) {
}
