package projectUser.ProjectSD.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import projectUser.ProjectSD.exception.AccessDeniedHandlerBean;
import projectUser.ProjectSD.repository.UserRepository;
import projectUser.ProjectSD.security.filter.AuthorizationFilter;
import projectUser.ProjectSD.security.filter.LoginFilter;
import projectUser.ProjectSD.security.service.UserDetailsServiceBean;
import projectUser.ProjectSD.security.util.SecurityConstants;

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
     * @param loginFilter         the filter for handling login requests
     * @param authorizationFilter the filter for handling authorization
     * @param accessDeniedHandler the handler for access denied exceptions
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            LoginFilter loginFilter,
            AuthorizationFilter authorizationFilter,
            AccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(SecurityConstants.AUTH_PATHS_TO_SKIP).permitAll()
                        .anyRequest().authenticated())
                .addFilter(loginFilter)
                .addFilterAfter(authorizationFilter, LoginFilter.class)
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
     * Configures the authentication manager for the application.
     *
     * @param http               the HttpSecurity object
     * @param passwordEncoder    the password encoder for encoding passwords
     * @param userDetailsService the user details service for loading user-specific data
     * @return the configured authentication manager
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    /**
     * Configures the password encoder for the application.
     *
     * @return the configured password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(SecurityConstants.PASSWORD_STRENGTH);
    }

    /**
     * Configures the user details service for the application.
     *
     * @param userRepository the repository for accessing person data
     * @return the configured user details service
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceBean(userRepository);
    }

    /**
     * Configures the authorization filter for the application.
     *
     * @param objectMapper the ObjectMapper for JSON serdes
     * @return the configured authorization filter
     */
    @Bean
    public AuthorizationFilter authorizationManager(ObjectMapper objectMapper) {
        return new AuthorizationFilter(objectMapper);
    }

    /**
     * Configures the login filter for the application.
     *
     * @param objectMapper          the ObjectMapper for JSON serialization and deserialization
     * @param authenticationManager the authentication manager for handling authentication requests
     * @return the configured login filter
     */
    @Bean
    public LoginFilter loginFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        return new LoginFilter(objectMapper, authenticationManager);
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
