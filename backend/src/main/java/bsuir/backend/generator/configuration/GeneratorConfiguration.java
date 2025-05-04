package bsuir.backend.generator.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "bsuir.backend.generator.storage.repository")
@EntityScan(basePackages = "bsuir.backend.generator.storage.entity")
public class GeneratorConfiguration {
}
