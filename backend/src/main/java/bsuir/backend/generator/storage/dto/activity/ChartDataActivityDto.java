package bsuir.backend.generator.storage.dto.activity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ChartDataActivityDto {
    private List<String> labels;
    private List<ChartDatasetActivityDto> datasets;
}