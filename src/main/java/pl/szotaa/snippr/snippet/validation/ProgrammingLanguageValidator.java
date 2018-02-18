package pl.szotaa.snippr.snippet.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProgrammingLanguageValidator implements ConstraintValidator<ProgrammingLanguage, String> {
    @Override
    public void initialize(ProgrammingLanguage programmingLanguage) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null || s.isEmpty()){
            return true;
        }

        for(ProgrammingLanguages language : ProgrammingLanguages.values()){
            if(language.name().equals(s)){
                return true;
            }
        }
        return false;
    }

    public enum ProgrammingLanguages {
        C, CPP, JAVA, CSHARP, PYTHON
    }
}
