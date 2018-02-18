package pl.szotaa.snippr.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.szotaa.snippr.common.ErrorResponse;
import pl.szotaa.snippr.user.exception.ApplicationUserAlreadyExistsException;
import pl.szotaa.snippr.user.exception.ApplicationUserNotFoundException;

@RestControllerAdvice
public class ApplicationUserControllerAdvice {

    @ExceptionHandler(ApplicationUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApplicationUserNotFoundException(ApplicationUserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(exception));
    }

    @ExceptionHandler(ApplicationUserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleApplicationUserAlreadyExistsException(ApplicationUserAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.of(exception));
    }
}
