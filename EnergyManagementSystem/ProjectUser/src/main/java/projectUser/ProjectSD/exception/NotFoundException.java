package projectUser.ProjectSD.exception;

/**
 * Exception thrown when a resource is not found.
 * This class extends RuntimeException to represent a runtime exception.
 */
public class NotFoundException extends RuntimeException {
    private final String message;

    public NotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
