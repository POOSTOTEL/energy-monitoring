package bsuir.backend.generator.controller;

import bsuir.backend.generator.service.storage.EnergyConsumptionStorageService;
import bsuir.backend.generator.storage.dto.activity.ChartDataActivityDto;
import bsuir.backend.generator.storage.dto.activity.ConsumptionRecordActivityDto;
import bsuir.backend.generator.storage.dto.activity.DeviceActivityDto;
import bsuir.backend.generator.storage.dto.activity.PeakConsumptionActivityDto;
import bsuir.backend.generator.storage.entity.EnergyConsumption;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ReportController {

    private final EnergyConsumptionStorageService energyConsumptionStorageService;

    @GetMapping(path = "/activity")
    public String getActivityReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        LocalDate finalDate = date;
        try {
            EnergyConsumption peak = energyConsumptionStorageService.findPeakConsumption(date)
                    .orElseThrow(
                            () -> new EntityNotFoundException(
                                    String.format("Пиковое потребление за дату %s не найдено", finalDate.toString())
                            ));
            EnergyConsumption low = energyConsumptionStorageService.findLowConsumption(date)
                    .orElseThrow(
                            () -> new EntityNotFoundException(
                                    String.format("Минимальное потребление за дату %s не найдено", finalDate.toString())
                            ));
            Double totalConsumption = energyConsumptionStorageService.calculateTotalConsumption(date);
            Double totalConsumptionTrend = energyConsumptionStorageService.calculateConsumptionTrend(date);
            ArrayList<DeviceActivityDto> devices = (ArrayList<DeviceActivityDto>) energyConsumptionStorageService.getActiveDevicesForDate(date);
            ArrayList<ConsumptionRecordActivityDto> records = (ArrayList<ConsumptionRecordActivityDto>) energyConsumptionStorageService.getActivityRecords(date);
            ChartDataActivityDto chartData = energyConsumptionStorageService.buildChartData(date);

            model.addAttribute("reportDate", date);
            model.addAttribute("devices", devices);
            model.addAttribute("consumptionRecords", records);
            model.addAttribute("totalConsumption", totalConsumption);
            model.addAttribute("peakConsumption",
                    PeakConsumptionActivityDto.builder()
                            .value(peak.getEnergyKwh())
                            .time(peak.getSaveTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                            .build());
            model.addAttribute("lowConsumption",
                    PeakConsumptionActivityDto.builder()
                            .value(low.getEnergyKwh())
                            .time(low.getSaveTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                            .build());
            model.addAttribute("trend", totalConsumptionTrend);
            model.addAttribute("chartLabels", chartData.getLabels());
            model.addAttribute("chartDatasets", chartData.getDatasets());
            return "activity";
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return "error";
        }
    }
}
