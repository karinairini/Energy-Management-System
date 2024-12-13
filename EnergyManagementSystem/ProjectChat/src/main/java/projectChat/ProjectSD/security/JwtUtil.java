package projectChat.ProjectSD.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.Properties;

/**
 * Utility class for JWT operations.
 * This class provides methods fot validated the JWT token.
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
     * Validates the JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getSigningKey(JwtUtil.secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception exception) {
            LOGGER.error("Error validating JWT token: {}", exception.getMessage());
            return false;
        }
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
