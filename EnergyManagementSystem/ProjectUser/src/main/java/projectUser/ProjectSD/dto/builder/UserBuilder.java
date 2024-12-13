package projectUser.ProjectSD.dto.builder;

import projectUser.ProjectSD.dto.user.RoleDTO;
import projectUser.ProjectSD.dto.user.UserRequestDTO;
import projectUser.ProjectSD.dto.user.UserResponseDTO;
import projectUser.ProjectSD.entity.UserEntity;

/**
 * UserBuilder is a utility class that provides static methods
 * for converting between UserEntity and its corresponding DTOs.
 */
public class UserBuilder {

    private UserBuilder() {
    }

    /**
     * Converts a UserEntity to a UserResponseDTO.
     *
     * @param userEntity the UserEntity to convert
     * @return the converted UserResponseDTO
     */
    public static UserResponseDTO toResponseDTO(UserEntity userEntity) {
        return new UserResponseDTO(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getAge(),
                userEntity.getEmail(),
                RoleDTO.valueOf(userEntity.getRole().name())
        );
    }

    /**
     * Converts a UserEntity to a UserRequestDTO.
     *
     * @param userEntity the UserEntity to convert
     * @return the converted UserRequestDTO
     */
    public static UserRequestDTO toRequestDTO(UserEntity userEntity) {
        return new UserRequestDTO(
                userEntity.getName(),
                userEntity.getAge(),
                userEntity.getEmail(),
                userEntity.getPassword()
        );
    }

    /**
     * Converts a UserRequestDTO to a UserEntity.
     *
     * @param userRequestDTO the UserRequestDTO to convert
     * @return the converted UserEntity
     */
    public static UserEntity toEntity(UserRequestDTO userRequestDTO) {
        return new UserEntity(
                userRequestDTO.getName(),
                userRequestDTO.getAge(),
                userRequestDTO.getEmail(),
                userRequestDTO.getPassword()
        );
    }
}
