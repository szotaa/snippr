package pl.szotaa.snippr.user.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(Long id){
        super("User with id: " + id + " not found");
    }

    public UserNotFoundException(String username){
        super("User with username: " + username + " not found");
    }
}
