package pl.szotaa.snippr.user.exception;

import pl.szotaa.snippr.common.AbstractConstraintViolationException;
import pl.szotaa.snippr.common.FieldError;

import java.util.Set;

public class ApplicationUserCreationFailedException extends AbstractConstraintViolationException {

    public ApplicationUserCreationFailedException(Set<FieldError> fieldErrors) {
        super("Failed to create new application user", fieldErrors);
    }
}
