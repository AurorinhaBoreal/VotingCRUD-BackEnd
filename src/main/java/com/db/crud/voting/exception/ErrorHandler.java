package com.db.crud.voting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


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
        return ResponseEntity.badRequest().body("The code informed for the enum isn't valid!");
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleErrorDataIntegrityViolationException(EntityExistsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleErrorAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(CannotFindEntityException.class)
    public ResponseEntity<String> handleErrorCannotFindEntityException(CannotFindEntityException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyVotedException.class)
    public ResponseEntity<String> handleErrorUserAlreadyVotedException(UserAlreadyVotedException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
}
