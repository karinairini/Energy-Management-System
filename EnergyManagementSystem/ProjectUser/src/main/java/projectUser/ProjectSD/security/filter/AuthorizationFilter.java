package projectUser.ProjectSD.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import projectUser.ProjectSD.exception.ExceptionBody;
import projectUser.ProjectSD.security.util.JwtUtil;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Filter for authorization of incoming HTTP requests using JWT tokens.
 * This filter extracts and validates JWT tokens from incoming requests, sets up authentication based on the
 * token's claims and handles authorization errors.
 */
public class AuthorizationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationFilter.class);
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new instance of the filter with the provided object mapper.
     *
     * @param objectMapper the object mapper to use for JSON serialization
     */
    public AuthorizationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Performs the filtering of incoming HTTP requests for authorization.
     *
     * @param request     the HTTP servlet request
     * @param response    the HTTP servlet response
     * @param filterChain the filter chain to continue processing the request
     * @throws ServletException if the servlet encounters a servlet-specific problem
     * @throws IOException      if an I/O error occurs while processing the request
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwtToken = JwtUtil.getTokenFromRequest(request);
        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = (Claims) Jwts.parser()
                    .verifyWith((SecretKey) JwtUtil.getSigningKey(JwtUtil.secretKey))
                    .build()
                    .parse(jwtToken)
                    .getPayload();
            String email = claims.getSubject();
            Collection<SimpleGrantedAuthority> authorities = ((List<String>) claims.get("role")).stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            this.onUnsuccessfulAuthorization(response, exception.getMessage());
        }
    }

    /**
     * Handles an unsuccessful authorization attempt by sending an appropriate HTTP response.
     *
     * @param response the HTTP servlet response
     * @param message  the error message to include in the response
     */
    private void onUnsuccessfulAuthorization(HttpServletResponse response, String message) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        try {
            objectMapper.writeValue(response.getWriter(), new ExceptionBody(message));
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        }
    }
}
