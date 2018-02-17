package pl.szotaa.snippr.snippet.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProgrammingLanguageValidator.class)
public @interface ProgrammingLanguage {
    String message() default "Specified syntax highlighting isn't supported";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
