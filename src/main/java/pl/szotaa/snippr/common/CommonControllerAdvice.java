package pl.szotaa.snippr.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorResponse>> handleConstraintViolationException(ConstraintViolationException ex){
        List<ErrorResponse> list = ex.getConstraintViolations().stream()
                .map(ErrorResponse::of).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(list);
    }
}
