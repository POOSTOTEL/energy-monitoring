package bsuir.backend.bot.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "report_access_log")
@Data
public class ReportAccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "telegram_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_report_user"))
    private TelegramUser user;

    @Column(name = "report_name", nullable = false)
    private String reportName;

    @Column(name = "access_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime accessTime;

    @Column(name = "parameters", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String parameters;

    @Column(name = "success", columnDefinition = "boolean default true")
    private Boolean success = true;
}