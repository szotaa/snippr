package pl.szotaa.snippr.snippet.domain;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.snippr.user.domain.User;

import java.time.Instant;

@JsonTest
@RunWith(SpringRunner.class)
public class SnippetJsonTest {

    @Autowired
    private JacksonTester<Snippet> jacksonTester;

    @Test
    public void serialize_AllFieldsProvided_JsonProperlySerialized() throws Exception {
        Instant now = Instant.now();

        Snippet snippetWithAllFieldsProvided = Snippet.builder()
                .id(1L)
                .title("Example Title")
                .content("Example content")
                .syntaxHighlighting("JAVA")
                .expiryDate(now.plusSeconds(1))
                .owner(new User())
                .dateAdded(now)
                .lastModified(now)
                .build();

        JsonContent<Snippet> snippetAsJson = jacksonTester.write(snippetWithAllFieldsProvided);

        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.id");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.title");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.content");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.syntaxHighlighting");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.expiryDate");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.owner");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.dateAdded");
        Assertions.assertThat(snippetAsJson).hasJsonPathValue("@.lastModified");

        Assertions.assertThat(snippetAsJson).extractingJsonPathStringValue("@.title")
                .isEqualTo("Example Title");
        Assertions.assertThat(snippetAsJson).extractingJsonPathStringValue("@.content")
                .isEqualTo("Example content");
        Assertions.assertThat(snippetAsJson).extractingJsonPathStringValue("@.syntaxHighlighting")
                .isEqualTo("JAVA");
        Assertions.assertThat(snippetAsJson).extractingJsonPathNumberValue("@.id")
                .isEqualTo(1);
    }

    @Test
    public void deserialize_AllFieldsProvided_JsonProperlyDeserialized() throws Exception {
        String snippetAsJson = "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"content\": \"Example content\",\n" +
                "  \"title\": \"Example title\",\n" +
                "  \"syntaxHighlighting\": \"JAVA\",\n" +
                "  \"expiryDate\": \"11111\",\n" +
                "  \"owner\": {\n" +
                "    \n" +
                "  },\n" +
                "  \"dateAdded\": \"11111\",\n" +
                "  \"lastModified\": \"111111\"\n" +
                "}";

        Snippet snippetFromJson = jacksonTester.parse(snippetAsJson).getObject();

        Assert.assertNull(snippetFromJson.getId());
        Assert.assertNull(snippetFromJson.getOwner());
        Assert.assertNull(snippetFromJson.getDateAdded());
        Assert.assertNull(snippetFromJson.getLastModified());

        Assert.assertNotNull(snippetFromJson.getTitle());
        Assert.assertNotNull(snippetFromJson.getContent());
        Assert.assertNotNull(snippetFromJson.getSyntaxHighlighting());
        Assert.assertNotNull(snippetFromJson.getExpiryDate());
    }
}
