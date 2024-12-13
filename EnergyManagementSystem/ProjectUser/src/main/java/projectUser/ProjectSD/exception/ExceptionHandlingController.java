package projectUser.ProjectSD.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Objects;

/**
 * Controller advice class for handling exceptions globally in the application.
 */
@RestControllerAdvice
public class ExceptionHandlingController {

    /**
     * Handles NotFoundException and returns an appropriate response with status NOT_FOUND (404).
     *
     * @param exception the NotFoundException to handle
     * @return an ExceptionBody object containing the error message
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionBody handleNotFoundException(NotFoundException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    /**
     * Handles MethodArgumentNotValidException and returns an appropriate response with status BAD_REQUEST (400).
     *
     * @param exception the MethodArgumentNotValidException to handle
     * @return an ExceptionBody object containing the error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final ExceptionBody handleInvalidArgument(MethodArgumentNotValidException exception) {
        return new ExceptionBody(getMessageFromInvalidArguments(exception));
    }

    /**
     * Handles any other unhandled exceptions and returns an appropriate response with status INTERNAL_SERVER_ERROR (500).
     *
     * @param exception the Exception to handle
     * @return an ExceptionBody object containing the error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public final ExceptionBody handleException(Exception exception) {
        return new ExceptionBody("EXCEPTION: " + exception.getMessage());
    }

    /**
     * Extracts and constructs an error message from the MethodArgumentNotValidException.
     *
     * @param exception the MethodArgumentNotValidException
     * @return the constructed error message
     */
    private String getMessageFromInvalidArguments(MethodArgumentNotValidException exception) {
        return Arrays.stream(Objects.requireNonNull(exception.getDetailMessageArguments()))
                .filter(message -> !((String) message).isEmpty())
                .toList()
                .toString();
    }
}
