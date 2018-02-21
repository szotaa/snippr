package pl.szotaa.snippr.common;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

@Getter
public class FieldError {

    private final String fieldName;
    private final String violation;

    public FieldError(String fieldName, String violation){
        this.fieldName = fieldName;
        this.violation = violation;
    }

    public static <T> Set<FieldError> toFieldErrorSet(Set<ConstraintViolation<T>> violations){
        Set<FieldError> fieldErrors = new HashSet<>();

        for(ConstraintViolation<T> violation : violations){
            String field = violation.getPropertyPath().toString();
            field = field.substring(field.indexOf(".") + 1, field.length());
            fieldErrors.add(new FieldError(field, violation.getMessage()));
        }
        return fieldErrors;
    }
}