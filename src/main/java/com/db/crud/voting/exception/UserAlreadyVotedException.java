package com.db.crud.voting.exception;

public class UserAlreadyVotedException extends RuntimeException{
    public UserAlreadyVotedException(String message) {
        super(message);
    }
}
