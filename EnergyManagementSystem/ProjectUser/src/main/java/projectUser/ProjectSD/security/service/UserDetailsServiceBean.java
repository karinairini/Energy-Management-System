package projectUser.ProjectSD.security.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import projectUser.ProjectSD.entity.UserEntity;
import projectUser.ProjectSD.exception.ExceptionCode;
import projectUser.ProjectSD.repository.UserRepository;

/**
 * Service class for loading user-specific data during authentication.
 * This class implements the UserDetailsService interface provided by Spring Security.
 */
public class UserDetailsServiceBean implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceBean(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user-specific data based on the provided email.
     *
     * @param email the email of the user
     * @return UserDetails object containing user information
     * @throws UsernameNotFoundException if the user is not found
     * @throws BadCredentialsException   if the user credentials are invalid
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .map(this::getUserDetails)
                .orElseThrow(() -> new BadCredentialsException(ExceptionCode.ERR099_INVALID_CREDENTIALS.getMessage()));
    }

    /**
     * Constructs a UserDetails object based on the provided UserEntity.
     *
     * @param user the UserEntity containing user information
     * @return UserDetails object containing user information
     */
    private UserDetails getUserDetails(UserEntity user) {
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
