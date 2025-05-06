package bsuir.backend.generator.storage.dto.activity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PeakConsumptionActivityDto {
    private Double value;
    private String time;
}
