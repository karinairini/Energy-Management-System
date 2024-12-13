package projectMonitoring.ProjectSD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectMonitoring.ProjectSD.entity.DeviceDataEntity;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for accessing DeviceDataEntity objects in the database.
 */
public interface DeviceDataRepository extends JpaRepository<DeviceDataEntity, UUID> {

    /**
     * Finds all device data entities associated with a device.
     *
     * @param deviceId the id of the device
     * @return a list of device data entities associated with the device
     */
    List<DeviceDataEntity> findByDeviceDeviceId(UUID deviceId);

    /**
     * Deletes all device data entities associated with a device.
     *
     * @param deviceId the id of the device
     */
    void deleteAllByDeviceDeviceId(UUID deviceId);
}
