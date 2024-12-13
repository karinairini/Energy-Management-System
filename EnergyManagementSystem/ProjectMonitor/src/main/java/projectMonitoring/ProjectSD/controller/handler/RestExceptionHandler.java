package projectMonitoring.ProjectSD.controller.handler;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import projectMonitoring.ProjectSD.controller.handler.exception.model.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controller advice class for handling exceptions globally in the application.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles NotFoundException and returns an appropriate response with status NOT_FOUND (404).
     *
     * @param exception the NotFoundException to handle
     * @return an ExceptionBody object containing the error message
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception,
                                                                     WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        Set<ConstraintViolation<?>> details = exception.getConstraintViolations();
        ExceptionHandlerResponseDTO errorInformation = new ExceptionHandlerResponseDTO(exception.getMessage(),
                status.getReasonPhrase(),
                status.value(),
                exception.getMessage(),
                details,
                request.getDescription(false));
        return handleExceptionInternal(
                exception,
                errorInformation,
                new HttpHeaders(),
                status,
                request
        );
    }

    /**
     * Handles MethodArgumentNotValidException and returns an appropriate response with status BAD_REQUEST (400).
     *
     * @param exception the MethodArgumentNotValidException to handle
     * @return an ExceptionBody object containing the error message
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        List<ObjectError> errs = exception.getBindingResult().getAllErrors();
        List<String> details = new ArrayList<>();
        for (ObjectError err : errs) {
            String fieldName = ((FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            details.add(fieldName + ":" + errorMessage);
        }
        ExceptionHandlerResponseDTO errorInformation = new ExceptionHandlerResponseDTO(exception.getParameter().getParameterName(),
                status.toString(),
                status.value(),
                MethodArgumentNotValidException.class.getSimpleName(),
                details,
                request.getDescription(false));
        return handleExceptionInternal(
                exception,
                errorInformation,
                new HttpHeaders(),
                status,
                request
        );
    }

    /**
     * Handles any other unhandled exceptions and returns an appropriate response with status INTERNAL_SERVER_ERROR (500).
     *
     * @param exception the Exception to handle
     * @return an ExceptionBody object containing the error message
     */
    @ExceptionHandler(value = {ResourceNotFoundException.class,
            DuplicateResourceException.class,
            EntityValidationException.class})
    protected ResponseEntity<Object> handleResourceNotFound(CustomException exception, WebRequest request) {
        ExceptionHandlerResponseDTO errorInformation = new ExceptionHandlerResponseDTO(exception.getResource(),
                exception.getStatus().getReasonPhrase(),
                exception.getStatus().value(),
                exception.getMessage(),
                exception.getValidationErrors(),
                request.getDescription(false));
        return handleExceptionInternal(
                exception,
                errorInformation,
                new HttpHeaders(),
                exception.getStatus(),
                request
        );
    }
}
