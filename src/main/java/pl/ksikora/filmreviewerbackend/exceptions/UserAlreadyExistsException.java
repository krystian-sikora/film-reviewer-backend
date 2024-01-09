package pl.ksikora.filmreviewerbackend.exceptions;

public class UserAlreadyExistsException extends IllegalStateException {
    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
