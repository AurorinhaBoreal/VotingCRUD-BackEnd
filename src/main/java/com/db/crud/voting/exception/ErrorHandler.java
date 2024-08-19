package com.db.crud.voting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleErrorConstraintViolation(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String answer = result.getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(answer);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleErrorHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("ERROR POPULATING FIELD!", e.getMessage());
        return ResponseEntity.badRequest().body("The code informed for the enum isn't valid!");
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleErrorDataIntegrityViolationException(EntityExistsException e) {
        log.error("ENTITY ALREADY EXISTS! ", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleErrorAuthorizationException(AuthorizationException e) {
        log.error("AUTHENTICATION ERROR! ", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(CannotFindEntityException.class)
    public ResponseEntity<String> handleErrorCannotFindEntityException(CannotFindEntityException e) {
        log.error("COULDN'T FIND ENTITY! ", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyVotedException.class)
    public ResponseEntity<String> handleErrorUserAlreadyVotedException(UserAlreadyVotedException e) {
        log.error("USER ALREADY VOTED! ", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }

    @ExceptionHandler(VoteConflictException.class)
    public ResponseEntity<String> handleErrorVoteConflictException(VoteConflictException e) {
        log.error("SORT VOTE CONFLICT!", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(AgendaEndedException.class)
    public ResponseEntity<String> handleErrorAgendaEndedException(AgendaEndedException e) {
        log.error("AGENDA ALREADY ENDED! ", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidEnumException.class)
    public ResponseEntity<String> handleInvalidEnumException(InvalidEnumException e) {
        log.error("THIS ENUM IS INVALID! ", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
