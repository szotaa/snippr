package pl.szotaa.snippr.snippet.exception;

import java.time.Instant;

public class SnippetExpiredException extends Exception {

    public SnippetExpiredException(long id, Instant expiryDate){
        super("Snippet with id: " + id + " expired at: " + expiryDate.getEpochSecond());
    }
}
