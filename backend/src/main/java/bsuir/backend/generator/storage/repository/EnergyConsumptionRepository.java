package bsuir.backend.generator.storage.repository;

import bsuir.backend.generator.storage.dto.activity.DeviceActivityDto;
import bsuir.backend.generator.storage.entity.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Long> {
    List<EnergyConsumption> findBySaveTimeBetween(LocalDateTime start, LocalDateTime end);

    List<EnergyConsumption> findByDeviceClassAndSaveTimeBetween(String deviceClass, LocalDateTime start, LocalDateTime end);

    List<EnergyConsumption> findAllBySaveTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT DISTINCT new bsuir.backend.generator.storage.dto.activity.DeviceActivityDto(" +
            "e.idInGroup, e.deviceName) " +
            "FROM EnergyConsumption e " +
            "WHERE e.saveTime >= :start AND e.saveTime < :end " +
            "ORDER BY e.deviceName")
    ArrayList<DeviceActivityDto> findDistinctDevicesByDate(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<EnergyConsumption> findAllBySaveTimeBetweenOrderBySaveTime(LocalDateTime start, LocalDateTime end);
}




