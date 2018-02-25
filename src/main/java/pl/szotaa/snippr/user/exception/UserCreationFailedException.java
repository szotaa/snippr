package pl.szotaa.snippr.user.exception;

import pl.szotaa.snippr.common.AbstractConstraintViolationException;
import pl.szotaa.snippr.common.FieldError;

import java.util.Set;

public class UserCreationFailedException extends AbstractConstraintViolationException {

    public UserCreationFailedException(Set<FieldError> fieldErrors) {
        super("Failed to create new application user", fieldErrors);
    }
}
