package pl.szotaa.snippr.snippet.exception;

import pl.szotaa.snippr.common.AbstractConstraintViolationException;
import pl.szotaa.snippr.common.FieldError;

import java.util.Set;

public class SnippetUpdateFailedException extends AbstractConstraintViolationException {

    public SnippetUpdateFailedException(long id, Set<FieldError> fieldErrors) {
        super("Failed to update snippet with id: " + id, fieldErrors);
    }
}
