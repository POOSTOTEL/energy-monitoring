package bsuir.backend.bot.service;

import bsuir.backend.generator.storage.entity.EnergyConsumption;
import bsuir.backend.generator.storage.repository.EnergyConsumptionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnergyDataService {
    private final EnergyConsumptionRepository repository;

    public EnergyDataService(EnergyConsumptionRepository repository) {
        this.repository = repository;
    }

    public void importEnergyData(List<EnergyConsumption> data) {
        repository.saveAll(data);
    }
}