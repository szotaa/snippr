package pl.szotaa.snippr.snippet.exception;

import pl.szotaa.snippr.common.AbstractConstraintViolationException;
import pl.szotaa.snippr.common.FieldError;

import java.util.Set;

public class SnippetCreationFailedException extends AbstractConstraintViolationException {

    public SnippetCreationFailedException(Set<FieldError> fieldErrors) {
        super("Failed to create new snippet", fieldErrors);
    }
}
