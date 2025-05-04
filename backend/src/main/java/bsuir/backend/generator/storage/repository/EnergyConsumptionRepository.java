package bsuir.backend.generator.storage.repository;

import bsuir.backend.generator.storage.entity.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Long> {
}
