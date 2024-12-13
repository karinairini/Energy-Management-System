package projectUser.ProjectSD.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import projectUser.ProjectSD.dto.auth.LoginRequestDTO;
import projectUser.ProjectSD.exception.ExceptionBody;
import projectUser.ProjectSD.exception.ExceptionCode;
import projectUser.ProjectSD.exception.NotFoundException;
import projectUser.ProjectSD.security.util.JwtUtil;
import projectUser.ProjectSD.security.util.SecurityConstants;

import java.io.IOException;

/**
 * Filter for handling user login authentication.
 * This filter intercepts login requests and attempts to authenticate the user using their credentials.
 * It also generates a JWT token upon successful authentication and sets it as a cookie in the response.
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new LoginFilter with the provided ObjectMapper and AuthenticationManager.
     *
     * @param objectMapper          the ObjectMapper used for JSON serialization/deserialization
     * @param authenticationManager the AuthenticationManager used for authenticating user credentials
     */
    public LoginFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        this.objectMapper = objectMapper;
        this.setAuthenticationManager(authenticationManager);
        this.setFilterProcessesUrl(SecurityConstants.LOGIN_URL);
    }

    /**
     * Attempts to authenticate the user using the provided credentials.
     *
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @return the authentication result
     * @throws AuthenticationException if authentication fails due to invalid credentials
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDTO authenticationRequest = objectMapper.readValue(
                    request.getInputStream(),
                    LoginRequestDTO.class
            );
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
            );
            return super.getAuthenticationManager().authenticate(authentication);
        } catch (BadCredentialsException badCredentialsException) {
            LOGGER.error(ExceptionCode.ERR099_INVALID_CREDENTIALS.getMessage());
            throw new BadCredentialsException(ExceptionCode.ERR099_INVALID_CREDENTIALS.getMessage());
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new NotFoundException(exception.getMessage());
        }
    }

    /**
     * Handles successful authentication by generating a JWT token and setting it as a cookie in the response.
     *
     * @param request    the HTTP servlet request
     * @param response   the HTTP servlet response
     * @param chain      the filter chain to continue processing the request
     * @param authResult the authentication result
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String accessToken = JwtUtil.generateJwtToken(
                ((User) authResult.getPrincipal()).getUsername(),
                authResult.getAuthorities()
        );
        response.addCookie(JwtUtil.buildCookie(SecurityConstants.JWT_TOKEN, accessToken));

        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * Handles unsuccessful authentication by sending an appropriate HTTP response with an error message.
     *
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @param failed   the authentication exception that occurred
     * @throws IOException if an I/O error occurs while processing the response
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        objectMapper.writeValue(response.getWriter(), new ExceptionBody(failed.getMessage()));
    }
}
