package bsuir.backend.generator.storage.dto.activity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ChartDatasetActivityDto {
    private String label;
    private List<Double> data;
    private String borderColor;
    private String backgroundColor;
    private Boolean fill;
    private Double tension;
}
