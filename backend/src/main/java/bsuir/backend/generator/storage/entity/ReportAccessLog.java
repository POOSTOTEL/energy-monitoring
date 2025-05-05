package bsuir.backend.generator.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@Table(schema = "public", name = "report_access_log")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ReportAccessLog {

    /**
     * Генерируемый идентификатор лога в БД
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Пользователь, запросивший отчёт
     */
    @JoinColumn(name = "telegram_user_id", referencedColumnName = "telegram_user_id")
    @OneToOne(fetch = FetchType.EAGER)
    private TelegramUser telegramUserId;

    /**
     * Тип отчёта
     */
    @Column(name = "report_name")
    private String reportName;

    /**
     * Дата и время запроса отчёта
     */
    @CreationTimestamp
    @Column(name = "access_time")
    private LocalDateTime accessTime;

    /**
     * Параметры, с которыми был запрошен отчёт
     */
    @Column(name = "parameters")
    private String parameters;
}
