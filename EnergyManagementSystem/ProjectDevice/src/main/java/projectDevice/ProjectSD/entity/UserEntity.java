package projectDevice.ProjectSD.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

/**
 * Represents a user entity.
 * This class encapsulates the user's id.
 * It serves as a POJO for mapping data to the "USER_ENTITY" table in the database.
 */
@Entity
public class UserEntity {
    @Id
    private UUID id;

    public UserEntity() {
    }

    public UserEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
