package projectMonitoring.ProjectSD.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import projectMonitoring.ProjectSD.entity.DeviceDataEntity;
import projectMonitoring.ProjectSD.repository.DeviceDataRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing Device data entities.
 * This class contains methods for:
 * - Deleting all device data by device id
 * - Retrieving device data values for a specific day
 */
@Service
public class DeviceDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDataService.class.getName());
    private final DeviceDataRepository deviceDataRepository;

    public DeviceDataService(DeviceDataRepository deviceDataRepository) {
        this.deviceDataRepository = deviceDataRepository;
    }

    @Transactional
    public void deleteDeviceDataByDeviceId(UUID deviceId) {
        LOGGER.info("Deleting device data by device id {}.", deviceId);
        deviceDataRepository.deleteAllByDeviceDeviceId(deviceId);
    }

    public List<Double> getDeviceDataValuesForDay(UUID deviceId, LocalDate date) {
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<DeviceDataEntity> deviceDataEntities = deviceDataRepository.findByDeviceDeviceId(deviceId)
                .stream()
                .filter(entity -> !entity.getTimestamp().isBefore(startOfDay) && entity.getTimestamp().isBefore(endOfDay))
                .toList();

        List<Double> values = deviceDataEntities.stream()
                .map(DeviceDataEntity::getValue)
                .collect(Collectors.toList());

        LOGGER.info("Found {} values for device id {} on the date {}: {}", values.size(), deviceId, date, values);
        return values;
    }
}
