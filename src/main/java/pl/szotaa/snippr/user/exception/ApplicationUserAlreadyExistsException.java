package pl.szotaa.snippr.user.exception;

public class ApplicationUserAlreadyExistsException extends Exception {

    public ApplicationUserAlreadyExistsException(String username){
        super("User with username: " + username + " already exists");
    }
}
