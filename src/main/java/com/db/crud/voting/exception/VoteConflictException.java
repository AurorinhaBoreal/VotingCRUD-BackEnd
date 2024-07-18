package com.db.crud.voting.exception;

public class VoteConflictException extends RuntimeException{
    public VoteConflictException(String message) {
        super(message);
    }
}