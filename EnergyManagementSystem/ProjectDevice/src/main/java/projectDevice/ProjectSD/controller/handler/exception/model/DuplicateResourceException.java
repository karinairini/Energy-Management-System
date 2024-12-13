package projectDevice.ProjectSD.controller.handler.exception.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;

/**
 * Custom exception for handling duplicate resources.
 */
public class DuplicateResourceException extends CustomException {
    private static final String MESSAGE = "Resource duplicated!";
    private static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public DuplicateResourceException(String resource) {
        super(MESSAGE, httpStatus, resource, new ArrayList<>());
    }
}
