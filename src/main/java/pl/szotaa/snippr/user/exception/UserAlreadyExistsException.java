package pl.szotaa.snippr.user.exception;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String username){
        super("User with username: " + username + " already exists");
    }
}
