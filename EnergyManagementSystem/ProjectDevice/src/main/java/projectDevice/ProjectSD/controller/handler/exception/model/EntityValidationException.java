package projectDevice.ProjectSD.controller.handler.exception.model;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Custom exception for when an entity could not be processed.
 */
public class EntityValidationException extends CustomException {
    private static final String MESSAGE = "Entity could not be processed!";
    private static final HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

    public EntityValidationException(String resource, List<String> errors) {
        super(MESSAGE, httpStatus, resource, errors);
    }
}
