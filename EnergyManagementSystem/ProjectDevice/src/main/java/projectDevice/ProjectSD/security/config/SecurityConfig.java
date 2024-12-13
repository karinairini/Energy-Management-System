package projectDevice.ProjectSD.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import projectDevice.ProjectSD.exception.AccessDeniedHandlerBean;
import projectDevice.ProjectSD.security.filter.AuthorizationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration class for security settings in the application.
 * This class defines various beans and configurations related to security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for the application.
     *
     * @param http                the HttpSecurity object to configure
     * @param authorizationFilter the filter for handling authorization
     * @param accessDeniedHandler the handler for access denied exceptions
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthorizationFilter authorizationFilter,
            AccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated())
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configures the CORS filter for the application.
     *
     * @return the configured CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * Configures the authorization filter for the application.
     *
     * @param objectMapper the ObjectMapper for JSON serialization and deserialization
     * @return the configured authorization filter
     */
    @Bean
    public AuthorizationFilter authorizationFilter(ObjectMapper objectMapper) {
        return new AuthorizationFilter(objectMapper);
    }

    /**
     * Configures the access denied handler for the application.
     *
     * @param objectMapper the ObjectMapper for JSON serialization and deserialization
     * @return the configured access denied handler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return new AccessDeniedHandlerBean(objectMapper);
    }
}
