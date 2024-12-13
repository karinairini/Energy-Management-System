package projectMonitoring.ProjectSD.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Custom AccessDeniedHandler implementation for handling access denied exceptions.
 * This class is responsible for setting the appropriate HTTP response when access is denied to a resource.
 */
public class AccessDeniedHandlerBean implements AccessDeniedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDeniedHandlerBean.class);
    private final ObjectMapper objectMapper;

    public AccessDeniedHandlerBean(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Handles the access denied exception by setting the HTTP response accordingly.
     *
     * @param request   the HttpServletRequest object
     * @param response  the HttpServletResponse object
     * @param exception the AccessDeniedException object
     */
    @Override
    public void handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AccessDeniedException exception
    ) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());

        try {
            objectMapper.writeValue(response.getWriter(), exception.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
