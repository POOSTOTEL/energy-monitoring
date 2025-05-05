package bsuir.backend.bot.repo;

import bsuir.backend.bot.model.ReportAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportAccessLogRepository extends JpaRepository<ReportAccessLog, Long> {
}
