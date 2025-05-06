package bsuir.backend.generator.storage.dto.activity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeviceConsumptionActivityDto {
    private String deviceId;
    private String deviceName;
    private Double value;
}
