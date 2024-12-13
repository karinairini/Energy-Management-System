package projectDevice.ProjectSD.security.util;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
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
     * Retrieves the signing key for JWT token verification.
     *
     * @param secretKey the secret key used for signing
     * @return the signing key
     */
    public static Key getSigningKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
