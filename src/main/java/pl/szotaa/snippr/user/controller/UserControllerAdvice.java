package pl.szotaa.snippr.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.szotaa.snippr.common.ErrorResponse;
import pl.szotaa.snippr.user.exception.UserAlreadyExistsException;
import pl.szotaa.snippr.user.exception.UserCreationFailedException;
import pl.szotaa.snippr.user.exception.UserNotFoundException;
import pl.szotaa.snippr.user.exception.UserUpdateFailedException;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApplicationUserNotFoundException(UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleApplicationUserAlreadyExistsException(UserAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exception));
    }

    @ExceptionHandler(UserCreationFailedException.class)
    public ResponseEntity<ErrorResponse> handleApplicationUserCreationFailedException(UserCreationFailedException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exception));
    }

    @ExceptionHandler(UserUpdateFailedException.class)
    public ResponseEntity<ErrorResponse> handleApplicationUserUpdateFailedException(UserUpdateFailedException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exception));
    }
}
