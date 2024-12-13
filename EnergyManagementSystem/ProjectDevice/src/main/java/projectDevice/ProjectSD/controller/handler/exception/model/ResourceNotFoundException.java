package projectDevice.ProjectSD.controller.handler.exception.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;

/**
 * Exception for when a resource is not found.
 */
public class ResourceNotFoundException extends CustomException {
    private static final String MESSAGE = "Resource not found!";
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String resource) {
        super(MESSAGE, httpStatus, resource, new ArrayList<>());
    }
}
