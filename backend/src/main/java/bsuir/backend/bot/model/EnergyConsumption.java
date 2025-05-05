package bsuir.backend.bot.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "energy_consumption")
@Data
public class EnergyConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_class", nullable = false)
    private String deviceClass;

    @Column(name = "device_type", nullable = false)
    private String deviceType;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(name = "id_in_group")
    private String idInGroup;

    @Column(name = "save_time", nullable = false)
    private LocalDateTime saveTime;

    @Column(name = "energy_kwh")
    private Float energyKwh;
}