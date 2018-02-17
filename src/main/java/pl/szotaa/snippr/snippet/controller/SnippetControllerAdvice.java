package pl.szotaa.snippr.snippet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.szotaa.snippr.common.ErrorResponse;
import pl.szotaa.snippr.snippet.exception.SnippetExpiredException;
import pl.szotaa.snippr.snippet.exception.SnippetNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class SnippetControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorResponse>> handleConstraintViolationException(ConstraintViolationException ex){
        List<ErrorResponse> list = ex.getConstraintViolations().stream()
                .map(SnippetControllerAdvice::of).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(list);
    }

    @ExceptionHandler(SnippetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSnippetNotFoundException(SnippetNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(of(exception));
    }

    @ExceptionHandler(SnippetExpiredException.class)
    public ResponseEntity<ErrorResponse> handleSnippetExpiredException(SnippetExpiredException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(of(exception));
    }

    private static ErrorResponse of(ConstraintViolation<?> constraintViolation){
        String field = constraintViolation.getPropertyPath().toString();
        field = field.substring(field.indexOf(".") + 1, field.length());
        return new ErrorResponse(field, constraintViolation.getMessage());
    }

    private static ErrorResponse of(Exception e){
        return new ErrorResponse(null, e.getMessage());
    }
}
