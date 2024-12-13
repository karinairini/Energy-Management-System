package projectUser.ProjectSD.exception;

import java.time.LocalDateTime;

/**
 * Represents the body of an exception response.
 * This class encapsulates the error message and the timestamp when the exception occurred.
 */
public class ExceptionBody {
    private final String message;
    private final LocalDateTime timestamp;

    public ExceptionBody(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ExceptionBody(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
