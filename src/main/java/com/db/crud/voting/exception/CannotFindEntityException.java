package com.db.crud.voting.exception;

public class CannotFindEntityException extends RuntimeException {
    public CannotFindEntityException(String message) {
        super(message);
    }
}
