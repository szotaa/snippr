package pl.szotaa.snippr.snippet.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;

public class NotBeforeCurrentTimeValidator implements ConstraintValidator<NotBeforeCurrentTime, Instant> {

    @Override
    public void initialize(NotBeforeCurrentTime notBeforeCurrentTime) {

    }

    @Override
    public boolean isValid(Instant instant, ConstraintValidatorContext constraintValidatorContext) {
        return instant == null || !instant.isBefore(Instant.now());
    }
}
