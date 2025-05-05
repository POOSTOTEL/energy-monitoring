package bsuir.backend.bot.repo;

import bsuir.backend.bot.model.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Long> {
    List<EnergyConsumption> findBySaveTimeBetween(LocalDateTime start, LocalDateTime end);
    List<EnergyConsumption> findByDeviceClassAndSaveTimeBetween(String deviceClass, LocalDateTime start, LocalDateTime end);
}