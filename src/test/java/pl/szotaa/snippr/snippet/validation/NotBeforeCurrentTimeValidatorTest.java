package pl.szotaa.snippr.snippet.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.szotaa.snippr.snippet.domain.Snippet;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Instant;
import java.util.Set;

public class NotBeforeCurrentTimeValidatorTest {

    private static Validator validator;
    private Snippet exampleSnippet;

    @BeforeClass
    public static void setUp(){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Before
    public void init(){
        exampleSnippet = Snippet.builder()
                .content("Example content")
                .title("Example title")
                .build();
    }

    @Test
    public void isValid_ExpiryDateAfterNow_NoValidationErrors() {
        Instant timeAfterNow = Instant.now().plusSeconds(1);
        exampleSnippet.setExpiryDate(timeAfterNow);
        Set<ConstraintViolation<Snippet>> constraintViolations = validator.validate(exampleSnippet);
        Assert.assertEquals(constraintViolations.size(), 0);
    }

    @Test
    public void isValid_ExpiryDateBeforeNow_OneValidationError() {
        Instant timeAfterNow = Instant.now().minusSeconds(1);
        exampleSnippet.setExpiryDate(timeAfterNow);
        Set<ConstraintViolation<Snippet>> constraintViolations = validator.validate(exampleSnippet);
        Assert.assertEquals(constraintViolations.size(), 1);
    }

    @Test
    public void isValid_ExpiryDateNull_NoValidationErrors() {
        Set<ConstraintViolation<Snippet>> constraintViolations = validator.validate(exampleSnippet);
        Assert.assertEquals(constraintViolations.size(), 0);
    }
}