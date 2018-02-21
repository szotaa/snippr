package pl.szotaa.snippr.common;

import lombok.Getter;

import java.util.Set;

public abstract class AbstractConstraintViolationException extends Exception {

    @Getter
    protected final Set<FieldError> fieldErrors;

    public AbstractConstraintViolationException(String message, Set<FieldError> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

}
