package pl.szotaa.snippr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pl.szotaa.snippr.snippet.controller.SnippetControllerTest;
import pl.szotaa.snippr.snippet.domain.SnippetJsonTest;
import pl.szotaa.snippr.snippet.service.SnippetServiceTest;
import pl.szotaa.snippr.snippet.validation.NotBeforeCurrentTimeValidatorTest;
import pl.szotaa.snippr.snippet.validation.ProgrammingLanguageValidatorTest;
import pl.szotaa.snippr.user.controller.ApplicationUserControllerTest;
import pl.szotaa.snippr.user.service.ApplicationUserServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		SnippetJsonTest.class,
		SnippetControllerTest.class,
		SnippetServiceTest.class,
		NotBeforeCurrentTimeValidatorTest.class,
		ProgrammingLanguageValidatorTest.class,
		ApplicationUserControllerTest.class,
		ApplicationUserServiceTest.class
})
public class SnipprApplicationTests {

	@Test
	public void contextLoads() {
	}

}
