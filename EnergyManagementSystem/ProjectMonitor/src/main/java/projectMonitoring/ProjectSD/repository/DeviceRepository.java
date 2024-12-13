package projectMonitoring.ProjectSD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectMonitoring.ProjectSD.entity.DeviceEntity;

import java.util.UUID;

/**
 * Repository interface for accessing DeviceEntity objects in the database.
 */
public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
}
