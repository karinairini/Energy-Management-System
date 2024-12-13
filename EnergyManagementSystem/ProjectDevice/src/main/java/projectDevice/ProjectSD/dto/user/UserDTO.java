package projectDevice.ProjectSD.dto.user;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a user.
 * This DTO encapsulates the id of the user.
 */
public class UserDTO implements Serializable {
    @NotNull(message = "The id of an user cannot be missing.")
    private UUID id;

    public UserDTO() {
    }

    public UserDTO(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
