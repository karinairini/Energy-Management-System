package projectUser.ProjectSD.exception;

/**
 * Enum representing custom exception codes with associated error messages.
 * Each enum constant encapsulates an error message template.
 */
public enum ExceptionCode {
    ERR001_USER_NOT_FOUND("User with id %s not found."),
    ERR002_EMAIL_NOT_FOUND("Email %s not found."),
    ERR099_INVALID_CREDENTIALS("Invalid credentials.");
    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
