package pl.szotaa.snippr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pl.szotaa.snippr.snippet.domain.SnippetJsonTest;
import pl.szotaa.snippr.snippet.validation.NotBeforeCurrentTimeValidatorTest;
import pl.szotaa.snippr.snippet.validation.ProgrammingLanguageValidatorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		SnippetJsonTest.class,
		NotBeforeCurrentTimeValidatorTest.class,
		ProgrammingLanguageValidatorTest.class
})
public class SnipprApplicationTests {

	@Test
	public void contextLoads() {
	}

}
