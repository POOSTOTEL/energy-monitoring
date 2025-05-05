package bsuir.backend.generator.service.storage;

import bsuir.backend.generator.storage.repository.ReportAccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReportAccessLogStorageService {
    private final ReportAccessLogRepository reportAccessLogRepository;
}
