package pl.szotaa.snippr.snippet.exception;

public class SnippetNotFoundException extends Exception {

    public SnippetNotFoundException(long id){
        super("Snippet with id: " + id + " not found");
    }
}
