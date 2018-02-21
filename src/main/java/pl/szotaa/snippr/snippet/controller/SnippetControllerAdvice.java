package pl.szotaa.snippr.snippet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.szotaa.snippr.common.ErrorResponse;
import pl.szotaa.snippr.snippet.exception.SnippetCreationFailedException;
import pl.szotaa.snippr.snippet.exception.SnippetExpiredException;
import pl.szotaa.snippr.snippet.exception.SnippetNotFoundException;
import pl.szotaa.snippr.snippet.exception.SnippetUpdateFailedException;

@RestControllerAdvice
public class SnippetControllerAdvice {

    @ExceptionHandler(SnippetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSnippetNotFoundException(SnippetNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception));
    }

    @ExceptionHandler(SnippetExpiredException.class)
    public ResponseEntity<ErrorResponse> handleSnippetExpiredException(SnippetExpiredException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exception));
    }

    @ExceptionHandler(SnippetCreationFailedException.class)
    public ResponseEntity<ErrorResponse> handleSnippetCreationFailedException(SnippetCreationFailedException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception));
    }

    @ExceptionHandler(SnippetUpdateFailedException.class)
    public ResponseEntity<ErrorResponse> handleSnippetUpdateFailedException(SnippetUpdateFailedException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception));
    }
}
