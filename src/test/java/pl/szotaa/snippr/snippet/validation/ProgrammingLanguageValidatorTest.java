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
import java.util.Set;

public class ProgrammingLanguageValidatorTest {

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
    public void isValid_CorrectProgrammingLanguage_NoValidationErrors(){
        exampleSnippet.setSyntaxHighlighting("CSHARP");
        Set<ConstraintViolation<Snippet>> constraintViolations = validator.validate(exampleSnippet);
        Assert.assertEquals(constraintViolations.size(), 0);
    }

    @Test
    public void isValid_NullProgrammingLanguage_NoValidationErrors(){
        Set<ConstraintViolation<Snippet>> constraintViolations = validator.validate(exampleSnippet);
        Assert.assertEquals(constraintViolations.size(), 0);
    }

    @Test
    public void isValid_IncorrectProgrammingLanguage_OneValidationError(){
        exampleSnippet.setSyntaxHighlighting("JAWA");
        Set<ConstraintViolation<Snippet>> constraintViolations = validator.validate(exampleSnippet);
        Assert.assertEquals(constraintViolations.size(), 1);
    }
}