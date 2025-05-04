package bsuir.backend.generator.storage.repository;

import bsuir.backend.generator.storage.entity.EnergyConsuption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyConsuptionRepository extends JpaRepository<EnergyConsuption, Long> {
}
