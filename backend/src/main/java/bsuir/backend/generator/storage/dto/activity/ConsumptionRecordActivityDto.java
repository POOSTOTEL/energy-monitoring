package bsuir.backend.generator.storage.dto.activity;

import bsuir.backend.generator.storage.enumeration.DeviceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class ConsumptionRecordActivityDto {
    private LocalDateTime time;
    private List<DeviceConsumptionActivityDto> consumptions;
    private DeviceStatus status;
}
