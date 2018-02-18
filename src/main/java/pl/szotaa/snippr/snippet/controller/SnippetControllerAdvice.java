package pl.szotaa.snippr.snippet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.szotaa.snippr.common.ErrorResponse;
import pl.szotaa.snippr.snippet.exception.SnippetExpiredException;
import pl.szotaa.snippr.snippet.exception.SnippetNotFoundException;

@RestControllerAdvice
public class SnippetControllerAdvice {

    @ExceptionHandler(SnippetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSnippetNotFoundException(SnippetNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(exception));
    }

    @ExceptionHandler(SnippetExpiredException.class)
    public ResponseEntity<ErrorResponse> handleSnippetExpiredException(SnippetExpiredException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.of(exception));
    }
}
