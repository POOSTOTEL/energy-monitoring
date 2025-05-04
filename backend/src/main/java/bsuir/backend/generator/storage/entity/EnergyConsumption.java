package bsuir.backend.generator.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "energy_consuption")
@Entity
@Data
public class EnergyConsumption {
    @Id
    private Long id;

}
