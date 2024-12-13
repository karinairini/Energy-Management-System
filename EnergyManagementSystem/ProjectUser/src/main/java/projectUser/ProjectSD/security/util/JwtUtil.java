package projectUser.ProjectSD.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Properties;

/**
 * Utility class for JWT operations.
 * This class provides methods for generating and parsing JWT tokens.
 */
@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    public static String secretKey;
    public static Integer tokenExpirationDays;

    static {
        loadProperties();
    }

    /**
     * Loads the JWT configuration properties (secret key and token expiration days) from
     * the 'application.properties' file.
     */
    private static void loadProperties() {
        try (InputStream inputStream = JwtUtil.class.getResourceAsStream("/application.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            secretKey = properties.getProperty("security.secret-key");
            tokenExpirationDays = Integer.parseInt(properties.getProperty("security.token-expiration-days"));

            LOGGER.info("JWT configuration loaded successfully.");
        } catch (IOException | NumberFormatException exception) {
            LOGGER.error("Error loading JWT configuration: {}", exception.getMessage());
            throw new RuntimeException("Failed to load JWT configuration from application.properties", exception);
        }
    }

    /**
     * Generates a JWT token for the given email and roles.
     *
     * @param email the email of the user
     * @param role  the roles of the user
     * @return the generated JWT token
     */
    public static String generateJwtToken(String email, Collection<? extends GrantedAuthority> role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role.stream().map(GrantedAuthority::getAuthority).toList())
                .expiration(JwtUtil.getExpirationDate(JwtUtil.tokenExpirationDays))
                .signWith(JwtUtil.getSigningKey(JwtUtil.secretKey))
                .compact();
    }

    /**
     * Builds a JWT token cookie with the given name and value.
     *
     * @param cookieName  the name of the cookie
     * @param cookieValue the value of the cookie
     * @return the constructed cookie
     */
    public static Cookie buildCookie(String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setMaxAge(JwtUtil.tokenExpirationDays);

        return cookie;
    }

    /**
     * Retrieves the JWT token from the request.
     *
     * @param request the HTTP servlet request
     * @return the JWT token extracted from the request
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization") == null
                ? null
                : request.getHeader("Authorization").split("Bearer ")[1];
    }

    /**
     * Retrieves the expiration date for the JWT token.
     *
     * @param tokenExpirationDays the number of days until token expiration
     * @return the expiration date
     */
    private static Date getExpirationDate(Integer tokenExpirationDays) {
        return Date.valueOf(LocalDate.now().plusDays(tokenExpirationDays));
    }

    /**
     * Retrieves the signing key for JWT token verification.
     *
     * @param secretKey the secret key used for signing
     * @return the signing key
     */
    public static Key getSigningKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
