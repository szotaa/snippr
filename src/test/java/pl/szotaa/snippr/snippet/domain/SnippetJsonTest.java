package pl.szotaa.snippr.snippet.domain;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.snippr.user.domain.ApplicationUser;

import java.time.Instant;

@JsonTest
@RunWith(SpringRunner.class)
public class SnippetJsonTest {

    @Autowired
    private JacksonTester<Snippet> json;

    @Test
    public void serialize_AllFieldsProvided_JsonIgnoreProperlyIgnored() throws Exception {
        Instant now = Instant.now();

        Snippet snippetWithAllFieldsProvided = Snippet.builder()
                .id(1L)
                .title("Example Title")
                .content("Example content")
                .syntaxHighlighting("JAVA")
                .expiryDate(now.plusSeconds(1))
                .owner(new ApplicationUser())
                .dateAdded(now)
                .lastModified(now)
                .build();

        JsonContent<Snippet> snippetAsJson = json.write(snippetWithAllFieldsProvided);

        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.title");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.content");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.syntaxHighlighting");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.expiryDate");

        Assertions.assertThat(snippetAsJson).extractingJsonPathStringValue("@.title")
                .isEqualTo("Example Title");
        Assertions.assertThat(snippetAsJson).extractingJsonPathStringValue("@.content")
                .isEqualTo("Example content");
        Assertions.assertThat(snippetAsJson).extractingJsonPathStringValue("@.syntaxHighlighting")
                .isEqualTo("JAVA");
        /*Assertions.assertThat(snippetAsJson).extractingJsonPathStringValue("@.expiryDate")
                .isEqualTo(snippetWithAllFieldsProvided.getExpiryDate());*/ //TODO: find out how to verify instant serialization

        Assertions.assertThat(snippetAsJson).doesNotHaveJsonPathValue("@.id");
        Assertions.assertThat(snippetAsJson).doesNotHaveJsonPathValue("@.owner");
        Assertions.assertThat(snippetAsJson).doesNotHaveJsonPathValue("@.dateAdded");
        Assertions.assertThat(snippetAsJson).doesNotHaveJsonPathValue("@.lastModified");
    }

    //TODO: deserialization test
}
