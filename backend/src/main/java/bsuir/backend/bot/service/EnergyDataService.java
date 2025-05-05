package bsuir.backend.bot.service;

import org.springframework.stereotype.Service;
import bsuir.backend.bot.model.EnergyConsumption;
import bsuir.backend.bot.repo.EnergyConsumptionRepository;
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