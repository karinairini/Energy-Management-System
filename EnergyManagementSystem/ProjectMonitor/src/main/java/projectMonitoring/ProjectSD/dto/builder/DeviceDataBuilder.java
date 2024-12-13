package projectMonitoring.ProjectSD.dto.builder;

import projectMonitoring.ProjectSD.entity.DeviceDataEntity;
import projectMonitoring.ProjectSD.entity.DeviceEntity;
import projectMonitoring.ProjectSD.service.message_consumer.DeviceDataMessage;

/**
 * DeviceDataBuilder is a utility class that provides static methods
 * for converting between DeviceDataMessage and DeviceDataEntity.
 */
public class DeviceDataBuilder {

    private DeviceDataBuilder() {
    }

    /**
     * Converts a DeviceDataMessage to a DeviceDataEntity.
     *
     * @param deviceDataMessage the DeviceDataMessage to convert
     * @param deviceEntity      the DeviceEntity to associate with the DeviceDataEntity
     * @return the DeviceDataEntity
     */
    public static DeviceDataEntity toEntity(DeviceDataMessage deviceDataMessage, DeviceEntity deviceEntity) {
        return new DeviceDataEntity(
                deviceDataMessage.getValue(),
                deviceDataMessage.getTimestamp(),
                deviceEntity
        );
    }
}
