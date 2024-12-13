package projectDevice.ProjectSD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectDevice.ProjectSD.entity.DeviceEntity;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for accessing DeviceEntity objects in the database.
 */
public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {

    /**
     * Retrieves a list of devices associated with the given user id.
     *
     * @param userId the id of the user whose devices to retrieve.
     * @return a list of DeviceEntity objects associated with the user.
     */
    List<DeviceEntity> findByUserId(UUID userId);
}
