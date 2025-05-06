package bsuir.backend.generator.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Table(schema = "public", name = "energy_consumption")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumption {

    /**
     * Генерируемый идентификатор потребления в БД
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Класс потребителя электроэнергии
     */
    @Column(name = "device_class")
    private String deviceClass;

    /**
     * Тип потребителя электроэнергии
     */
    @Column(name = "device_type")
    private String deviceType;

    /**
     * Название потребителя электроэнергии
     */
    @Column(name = "device_name")
    private String deviceName;

    /**
     * Идентификатор внутри группы потребителей
     */
    @Column(name = "id_in_group")
    private String idInGroup;

    /**
     * Дата и время снятия показателей потребителей электроэнергии
     */
    @Column(name = "save_time")
    private LocalDateTime saveTime;

    /**
     * Показатель потребления электроэнергии
     */
    @Column(name = "energy_kwh")
    private Double energyKwh;
}
