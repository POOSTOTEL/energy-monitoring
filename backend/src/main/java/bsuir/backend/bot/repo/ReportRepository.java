package bsuir.backend.bot.repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByUniqueField(String uniqueField);
}
