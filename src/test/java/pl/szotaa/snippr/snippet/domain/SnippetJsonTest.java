package pl.szotaa.snippr.snippet.domain;

import com.fasterxml.jackson.datatype.jsr310.DecimalUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.snippr.user.domain.ApplicationUser;

import java.time.Instant;

@Slf4j
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
        Assertions.assertThat(snippetAsJson).extractingJsonPathStringValue("@.expiryDate")
                .contains(Long.toString(now.getEpochSecond()));

        Assertions.assertThat(snippetAsJson).doesNotHaveJsonPathValue("@.id");
        Assertions.assertThat(snippetAsJson).doesNotHaveJsonPathValue("@.owner");
        Assertions.assertThat(snippetAsJson).doesNotHaveJsonPathValue("@.dateAdded");
        Assertions.assertThat(snippetAsJson).doesNotHaveJsonPathValue("@.lastModified");
    }

    @Test
    public void deserialize_AllFieldsProvided_JsonIgnoreProperlyIgnored() throws Exception {
        String json = "{\n" +
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

        Snippet snippetFromJson = this.json.parse(json).getObject();

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
