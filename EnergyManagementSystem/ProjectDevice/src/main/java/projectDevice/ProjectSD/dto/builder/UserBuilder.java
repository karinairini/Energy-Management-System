package projectDevice.ProjectSD.dto.builder;

import projectDevice.ProjectSD.dto.user.UserDTO;
import projectDevice.ProjectSD.entity.UserEntity;

/**
 * UserBuilder is a utility class that provides static methods
 * for converting between UserEntity and its corresponding DTO.
 */
public class UserBuilder {

    private UserBuilder() {
    }

    /**
     * Converts a UserEntity to a UserDTO.
     *
     * @param userEntity the UserEntity to convert
     * @return the converted UserDTO
     */
    public static UserDTO toDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getId());
    }

    /**
     * Converts a UserDTO to a UserEntity.
     *
     * @param userDTO the UserDTO to convert
     * @return the converted UserEntity
     */
    public static UserEntity toEntity(UserDTO userDTO) {
        return new UserEntity(userDTO.getId());
    }
}
