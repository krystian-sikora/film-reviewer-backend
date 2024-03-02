package pl.ksikora.filmreviewerbackend.user;

public class UserAlreadyExistsException extends IllegalStateException {
    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
