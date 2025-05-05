package bsuir.backend.generator.storage.repository;

import bsuir.backend.generator.storage.entity.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Long> {
    List<EnergyConsumption> findBySaveTimeBetween(LocalDateTime start, LocalDateTime end);
    List<EnergyConsumption> findByDeviceClassAndSaveTimeBetween(String deviceClass, LocalDateTime start, LocalDateTime end);
}
