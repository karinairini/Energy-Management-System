package projectDevice.ProjectSD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectDevice.ProjectSD.entity.UserEntity;

import java.util.UUID;

/**
 * Repository interface for accessing UserEntity objects in the database.
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
