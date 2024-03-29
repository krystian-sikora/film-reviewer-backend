package pl.ksikora.filmreviewerbackend.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.ksikora.filmreviewerbackend.user.UserAlreadyExistsException;

import java.util.Map;
import java.util.stream.Collectors;

import static pl.ksikora.filmreviewerbackend.advice.Error.BAD_CREDENTIALS;
import static pl.ksikora.filmreviewerbackend.advice.Error.USERNAME_NOT_FOUND;
import static pl.ksikora.filmreviewerbackend.advice.Error.USER_ALREADY_EXISTS;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(fieldError -> fieldError.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField() + "Error",
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransactionSystemException.class)
    public String handleTransactionSystemException(TransactionSystemException ex) {
        return getExceptionMessage(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return USER_ALREADY_EXISTS.name();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return USERNAME_NOT_FOUND.name();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException ex) {
        return BAD_CREDENTIALS.name();
    }

    private static String getExceptionMessage(TransactionSystemException ex) {
        String msg = ex.getCause().getCause().getMessage();
        return msg.substring(msg.indexOf("messageTemplate='"), msg.indexOf("'}")).substring(17);
    }
}
