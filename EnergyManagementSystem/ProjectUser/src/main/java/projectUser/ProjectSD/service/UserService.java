package projectUser.ProjectSD.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import projectUser.ProjectSD.dto.builder.UserBuilder;
import projectUser.ProjectSD.dto.user.UserRequestDTO;
import projectUser.ProjectSD.dto.user.UserResponseDTO;
import projectUser.ProjectSD.entity.Role;
import projectUser.ProjectSD.entity.UserEntity;
import projectUser.ProjectSD.exception.ExceptionCode;
import projectUser.ProjectSD.exception.NotFoundException;
import projectUser.ProjectSD.repository.UserRepository;
import projectUser.ProjectSD.security.util.SecurityConstants;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing User entities.
 * This class contains methods for:
 * - Retrieving all users
 * - Retrieving a user by id
 * - Saving a user
 * - Updating a user
 * - Deleting a user
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Autowired
    public UserService(UserRepository userRepository, HttpServletRequest request) {
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
        this.request = request;
    }

    /**
     * Retrieves a user by its email.
     *
     * @param email the email of the user to retrieve
     * @return UserResponseDTO containing user information
     * @throws NotFoundException if the user is not found
     */
    public UserResponseDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserBuilder::toResponseDTO)
                .orElseThrow(() -> new NotFoundException(String.format(
                        ExceptionCode.ERR002_EMAIL_NOT_FOUND.getMessage(),
                        email
                )));
    }

    /**
     * Retrieves all users from the database.
     *
     * @return list of UserResponseDTO containing user information
     */
    public List<UserResponseDTO> getUsers() {
        LOGGER.info("Getting all users.");
        List<UserEntity> userList = userRepository.findAllClients();

        return userList.stream()
                .map(UserBuilder::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all admins from the database.
     *
     * @return list of UserResponseDTO containing admin information
     */
    public List<UserResponseDTO> getAdmins() {
        LOGGER.info("Getting all admins.");
        List<UserEntity> adminList = userRepository.findAllAdmins();

        return adminList.stream()
                .map(UserBuilder::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by its id.
     *
     * @param id the id of the user to retrieve
     * @return UserResponseDTO containing user information
     * @throws NotFoundException if the user is not found
     */
    public UserResponseDTO getUserById(UUID id) {
        LOGGER.info("Getting user with id {}.", id);
        UserEntity userEntity = findUserByIdOrThrow(id);

        return UserBuilder.toResponseDTO(userEntity);
    }

    /**
     * Saves a new user to the database.
     *
     * @param userRequestDTO the user information to save
     * @return UserResponseDTO containing the saved user information
     */
    @Transactional
    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        UserEntity userToBeSaved = UserBuilder.toEntity(userRequestDTO);

        String passwordToBeAdded = userToBeSaved.getPassword();
        userToBeSaved.setPassword(
                new BCryptPasswordEncoder(SecurityConstants.PASSWORD_STRENGTH)
                        .encode(passwordToBeAdded));

        userToBeSaved.setRole(Role.CLIENT);

        UserEntity userSaved = userRepository.save(userToBeSaved);
        LOGGER.info("User with id {} was saved in database.", userSaved.getId());

        String url = "http://reverse-proxy/api/energy-management-device/device/user";
        //String url = "http://localhost:8081/api/energy-management/device/user";
        sendUserToExternalApi(userSaved, url, HttpMethod.POST, null);

        return UserBuilder.toResponseDTO(userSaved);
    }

    /**
     * Updates an existing user in the database.
     *
     * @param id             the id of the user to update
     * @param userRequestDTO the new user information
     * @return UserResponseDTO containing the updated user information
     * @throws NotFoundException if the user is not found
     */
    @Transactional
    public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
        UserEntity existingUser = findUserByIdOrThrow(id);
        UserEntity userToBeUpdated = UserBuilder.toEntity(userRequestDTO);

        userToBeUpdated.setId(id);
        userToBeUpdated.setRole(Role.CLIENT);
        if (userToBeUpdated.getPassword() == null) {
            userToBeUpdated.setPassword(existingUser.getPassword());
        } else {
            userToBeUpdated.setPassword(
                    new BCryptPasswordEncoder(SecurityConstants.PASSWORD_STRENGTH)
                            .encode(userToBeUpdated.getPassword()));
        }

        UserEntity userUpdated = userRepository.save(userToBeUpdated);
        LOGGER.info("User with id {} was updated in database.", id);

        return UserBuilder.toResponseDTO(userUpdated);
    }

    /**
     * Deletes a user by its id from the database.
     *
     * @param id the id of the user to delete
     * @throws NotFoundException if the user is not found
     */
    @Transactional
    public void deleteUser(UUID id) {
        UserEntity userToBeDeleted = findUserByIdOrThrow(id);

        String url = "http://reverse-proxy/api/energy-management-device/device/user/{id}";
        //String url = "http://localhost:8081/api/energy-management/device/user/{id}";
        sendUserToExternalApi(userToBeDeleted, url, HttpMethod.DELETE, id);

        userRepository.deleteById(id);
        LOGGER.info("User with id {} was deleted from database.", id);
    }

    /**
     * Finds a user by its id.
     *
     * @param id the id of the user to find
     * @return UserEntity if found
     * @throws NotFoundException if the user is not found
     */
    private UserEntity findUserByIdOrThrow(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found in database.", id);
            return new NotFoundException(
                    String.format(ExceptionCode.ERR001_USER_NOT_FOUND.getMessage(), id)
            );
        });
    }

    /**
     * Sends a user entity to an external API using the specified HTTP method.
     *
     * @param user   the user entity to send to the external API
     * @param url    the URL of the external API endpoint
     * @param method the HTTP method to use for the request (POST or DELETE)
     * @param id     the id of the user; necessary for DELETE requests to specify the resource
     */
    private void sendUserToExternalApi(UserEntity user, String url, HttpMethod method, UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = retrieveJwtToken();
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }

        HttpEntity<UserEntity> requestEntity = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<Void> response = executeApiCall(url, method, requestEntity, id);
            if (response.getStatusCode().is2xxSuccessful()) {
                logApiResult(method, user.getId());
            } else {
                LOGGER.warn("Failed to send request to external API. Status code: {}", response.getStatusCode());
            }
        } catch (Exception exception) {
            LOGGER.error("Error occurred while calling external API: {}", exception.getMessage());
        }
    }

    private String retrieveJwtToken() {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt-token".equals(cookie.getName())) { // Replace "jwt-token" with your cookie name
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Executes the API call to the specified URL using the given HTTP method.
     *
     * @param url           the URL of the external API endpoint
     * @param method        the HTTP method to use for the request (POST or DELETE)
     * @param requestEntity the entity to be sent in the request
     * @param id            the id of the user; required for DELETE requests to specify the resource
     * @return the response from the API call
     * @throws RestClientException if an error occurs during the API call
     */
    private ResponseEntity<Void> executeApiCall(String url,
                                                HttpMethod method,
                                                HttpEntity<UserEntity> requestEntity,
                                                UUID id) {
        return (method == HttpMethod.DELETE && id != null)
                ? restTemplate.exchange(url, method, requestEntity, Void.class, id)
                : restTemplate.exchange(url, method, requestEntity, Void.class);
    }

    /**
     * Logs the success message based on the HTTP method used.
     *
     * @param method the HTTP method that was used
     * @param userId the id of the user that was sent to the external API
     */
    private void logApiResult(HttpMethod method, UUID userId) {
        if (method == HttpMethod.POST) {
            LOGGER.info("User with id {} was successfully sent to external API.", userId);
        } else if (method == HttpMethod.DELETE) {
            LOGGER.info("User with id {} was successfully deleted from external API.", userId);
        }
    }
}
