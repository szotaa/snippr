package pl.szotaa.snippr.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final Instant timestamp = Instant.now();
    private final String exception;
    private Set<FieldError> fieldErrors;

    public ErrorResponse(AbstractConstraintViolationException exception){
        this.exception = exception.getClass().getSimpleName();
        this.fieldErrors = exception.getFieldErrors();
    }

    public ErrorResponse(Exception exception){
        this.exception = exception.getClass().getName();
    }
}
