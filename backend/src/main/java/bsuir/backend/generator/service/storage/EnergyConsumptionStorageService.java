package bsuir.backend.generator.service.storage;

import bsuir.backend.generator.storage.dto.activity.ChartDataActivityDto;
import bsuir.backend.generator.storage.dto.activity.ChartDatasetActivityDto;
import bsuir.backend.generator.storage.dto.activity.ConsumptionRecordActivityDto;
import bsuir.backend.generator.storage.dto.activity.DeviceActivityDto;
import bsuir.backend.generator.storage.dto.activity.DeviceConsumptionActivityDto;
import bsuir.backend.generator.storage.entity.EnergyConsumption;
import bsuir.backend.generator.storage.enumeration.DeviceStatus;
import bsuir.backend.generator.storage.repository.EnergyConsumptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class EnergyConsumptionStorageService {
    private final EnergyConsumptionRepository energyConsumptionRepository;

    public Optional<EnergyConsumption> findPeakConsumption(LocalDate date) {
        List<EnergyConsumption> consumptions = energyConsumptionRepository
                .findAllBySaveTimeBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        return consumptions.stream()
                .max(Comparator.comparing(EnergyConsumption::getEnergyKwh));
    }

    public Optional<EnergyConsumption> findLowConsumption(LocalDate date) {
        List<EnergyConsumption> consumptions = energyConsumptionRepository
                .findAllBySaveTimeBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        return consumptions.stream()
                .min(Comparator.comparing(EnergyConsumption::getEnergyKwh));
    }

    public Double calculateTotalConsumption(LocalDate date) {
        List<EnergyConsumption> consumptions = energyConsumptionRepository
                .findAllBySaveTimeBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());

        if (consumptions.isEmpty()) {
            throw new EntityNotFoundException("Нет данных о потреблении за " + date);
        }

        return consumptions.stream()
                .mapToDouble(EnergyConsumption::getEnergyKwh)
                .sum();
    }

    public Double calculateConsumptionTrend(LocalDate date) {
        // Получаем данные за текущие сутки
        Double currentDayConsumption = calculateTotalConsumption(date);

        // Получаем данные за предыдущие сутки
        LocalDate previousDay = date.minusDays(1);
        Double previousDayConsumption = calculateTotalConsumption(previousDay);

        // Обработка случая, когда нет данных за предыдущий день
        if (previousDayConsumption == null || previousDayConsumption == 0) {
            return 0.0;
        }

        // Рассчитываем процентное изменение
        double trend = ((currentDayConsumption - previousDayConsumption) / previousDayConsumption) * 100;

        // Округляем до 2 знаков после запятой
        return Math.round(trend * 100.0) / 100.0;
    }

    public List<DeviceActivityDto> getActiveDevicesForDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        return energyConsumptionRepository.findDistinctDevicesByDate(startOfDay, endOfDay);
    }

    public List<ConsumptionRecordActivityDto> getActivityRecords(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        // Получаем все записи за указанный день
        List<EnergyConsumption> consumptions = energyConsumptionRepository
                .findAllBySaveTimeBetweenOrderBySaveTime(start, end);

        // Группируем по времени
        Map<LocalDateTime, List<EnergyConsumption>> byTime = consumptions.stream()
                .collect(Collectors.groupingBy(
                        EnergyConsumption::getSaveTime
                ));

        // Получаем список всех уникальных устройств
        Set<String> deviceIds = consumptions.stream()
                .map(EnergyConsumption::getIdInGroup)
                .collect(Collectors.toSet());

        return byTime.entrySet().stream()
                .map(entry -> buildTimeRecord(entry.getKey(), entry.getValue(), deviceIds))
                .sorted(Comparator.comparing(ConsumptionRecordActivityDto::getTime))
                .collect(Collectors.toList());
    }

    private ConsumptionRecordActivityDto buildTimeRecord(LocalDateTime time,
                                                         List<EnergyConsumption> ecList,
                                                         Set<String> allDeviceIds) {
        // Создаем мапу для быстрого доступа к потреблению по deviceId
        Map<String, Double> consumptionMap = ecList.stream()
                .collect(Collectors.toMap(
                        EnergyConsumption::getIdInGroup,
                        EnergyConsumption::getEnergyKwh
                ));

        // Создаем список устройств с их потреблением
        List<DeviceConsumptionActivityDto> consumptions = allDeviceIds.stream()
                .map(deviceId -> DeviceConsumptionActivityDto.builder()
                        .deviceId(deviceId)
                        .deviceName(ecList.stream()
                                .filter(ec -> ec.getIdInGroup().equals(deviceId))
                                .findFirst()
                                .map(EnergyConsumption::getDeviceName)
                                .orElse(""))
                        .value(consumptionMap.getOrDefault(deviceId, 0.0))
                        .build())
                .collect(Collectors.toList());

        // Определяем статус
        DeviceStatus status = calculateStatus(consumptionMap.values());

        return ConsumptionRecordActivityDto.builder()
                .time(time)
                .consumptions(consumptions)
                .status(status)
                .build();
    }

    private DeviceStatus calculateStatus(Collection<Double> values) {
        boolean allZero = values.stream().allMatch(v -> v == null || v == 0);
        boolean anyActive = values.stream().anyMatch(v -> v != null && v > 0.5);

        if (allZero) return DeviceStatus.INACTIVE;
        if (anyActive) return DeviceStatus.ACTIVE;
        return DeviceStatus.PARTIAL;
    }

    public ChartDataActivityDto buildChartData(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        // Получаем все записи за указанный день
        List<EnergyConsumption> consumptions = energyConsumptionRepository
                .findAllBySaveTimeBetweenOrderBySaveTime(start, end);

        // Группируем по устройствам
        Map<String, List<EnergyConsumption>> byDevice = consumptions.stream()
                .collect(Collectors.groupingBy(
                        ec -> ec.getDeviceName() + " (" + ec.getIdInGroup() + ")"
                ));

        // Собираем уникальные временные метки (с точностью до минуты)
        List<String> labels = consumptions.stream()
                .map(ec -> ec.getSaveTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES))
                .distinct()
                .sorted()
                .map(time -> time.format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());

        // Создаем наборы данных для каждого устройства
        List<ChartDatasetActivityDto> datasets = new ArrayList<>();
        String[] colors = {"#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#9966FF"};

        int colorIndex = 0;
        for (Map.Entry<String, List<EnergyConsumption>> entry : byDevice.entrySet()) {
            String deviceName = entry.getKey();
            List<EnergyConsumption> deviceConsumptions = entry.getValue();

            // Группируем потребление по времени с агрегацией (суммированием)
            Map<LocalTime, Double> timeToValue = deviceConsumptions.stream()
                    .collect(Collectors.groupingBy(
                            ec -> ec.getSaveTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES),
                            Collectors.summingDouble(EnergyConsumption::getEnergyKwh)
                    ));

            // Заполняем данные для всех временных меток
            List<Double> data = labels.stream()
                    .map(label -> {
                        LocalTime time = LocalTime.parse(label);
                        return timeToValue.getOrDefault(time, 0.0);
                    })
                    .collect(Collectors.toList());

            datasets.add(ChartDatasetActivityDto.builder()
                    .label(deviceName)
                    .data(data)
                    .borderColor(colors[colorIndex % colors.length])
                    .backgroundColor(colors[colorIndex % colors.length] + "33")
                    .fill(true)
                    .tension(0.1)
                    .build());

            colorIndex++;
        }

        return ChartDataActivityDto.builder()
                .labels(labels)
                .datasets(datasets)
                .build();
    }
}
