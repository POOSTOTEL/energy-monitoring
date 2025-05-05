package bsuir.backend.bot.service;

import bsuir.backend.bot.repo.ReportRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportImportService {

    private final ReportRepository reportRepository;

    public ReportImportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void importReports(List<Report> reports) {
        for (Report report: reports){
            if (!reportRepository.existsByUniqueField(report.getUniqueField)){
                reportRepository.save(report);
            }
        }
    }

}
