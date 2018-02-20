package pl.szotaa.snippr.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.ConstraintViolation;
import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final Instant timestamp = Instant.now();
    private String fieldName;
    private String message;

    private ErrorResponse(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public static ErrorResponse of(ConstraintViolation<?> constraintViolation){
        String field = constraintViolation.getPropertyPath().toString();
        field = field.substring(field.indexOf(".") + 1, field.length());
        return new ErrorResponse(field, constraintViolation.getMessage());
    }

    public static ErrorResponse of(Exception e){
        return new ErrorResponse(null, e.getMessage());
    }
}
