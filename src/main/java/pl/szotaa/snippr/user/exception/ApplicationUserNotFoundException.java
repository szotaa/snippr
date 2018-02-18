package pl.szotaa.snippr.user.exception;

public class ApplicationUserNotFoundException extends Exception {

    public ApplicationUserNotFoundException(Long id){
        super("User with id: " + id + " not found");
    }

    public ApplicationUserNotFoundException(String username){
        super("User with username: " + username + " not found");
    }
}
