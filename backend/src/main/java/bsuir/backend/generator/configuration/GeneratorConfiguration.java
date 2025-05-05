package bsuir.backend.generator.configuration;

import bsuir.backend.generator.service.storage.EnergyConsumptionStorageService;
import bsuir.backend.generator.service.storage.ReportAccessLogStorageService;
import bsuir.backend.generator.service.storage.TelegramUserStorageService;
import bsuir.backend.generator.storage.repository.EnergyConsumptionRepository;
import bsuir.backend.generator.storage.repository.ReportAccessLogRepository;
import bsuir.backend.generator.storage.repository.TelegramUserRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "bsuir.backend.generator.storage.repository")
@EntityScan(basePackages = "bsuir.backend.generator.storage.entity")
public class GeneratorConfiguration {

    @Bean
    public EnergyConsumptionStorageService energyConsumptionStorageService(EnergyConsumptionRepository energyConsumptionRepository) {
        return new EnergyConsumptionStorageService(energyConsumptionRepository);
    }

    @Bean
    public ReportAccessLogStorageService reportAccessLogStorageService(ReportAccessLogRepository reportAccessLogRepository) {
        return new ReportAccessLogStorageService(reportAccessLogRepository);
    }

    @Bean
    public TelegramUserStorageService telegramUserStorageService(TelegramUserRepository telegramUserRepository) {
        return new TelegramUserStorageService(telegramUserRepository);
    }
}
