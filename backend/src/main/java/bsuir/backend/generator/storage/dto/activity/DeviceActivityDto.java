package bsuir.backend.generator.storage.dto.activity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeviceActivityDto {
    private String id;
    private String name;
}
