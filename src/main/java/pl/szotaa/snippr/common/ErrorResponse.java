package pl.szotaa.snippr.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final Instant timestamp = Instant.now();
    private String fieldName;
    private String message;

    public ErrorResponse(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
