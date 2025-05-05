package bsuir.backend.generator.storage.repository;

import bsuir.backend.generator.storage.entity.ReportAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportAccessLogRepository extends JpaRepository<ReportAccessLog,Long> {
}
