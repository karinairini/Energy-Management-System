package projectMonitoring.ProjectSD.dto.builder;

import projectMonitoring.ProjectSD.entity.DeviceEntity;
import projectMonitoring.ProjectSD.service.message_consumer.DeviceMessage;

/**
 * DeviceBuilder is a utility class that provides static methods
 * for converting between DeviceMessage and DeviceEntity.
 */
public class DeviceBuilder {

    private DeviceBuilder() {
    }

    /**
     * Converts a DeviceMessage to a DeviceEntity.
     *
     * @param deviceMessage the DeviceMessage to convert
     * @return the converted DeviceEntity
     */
    public static DeviceEntity toEntity(DeviceMessage deviceMessage) {
        return new DeviceEntity(
                deviceMessage.getDeviceId(),
                deviceMessage.getDescription(),
                deviceMessage.getMaximumHourlyConsumption(),
                deviceMessage.getClientId()
        );
    }
}
