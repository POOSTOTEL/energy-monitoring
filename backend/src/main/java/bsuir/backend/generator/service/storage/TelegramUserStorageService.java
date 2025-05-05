package bsuir.backend.generator.service.storage;

import bsuir.backend.generator.storage.repository.TelegramUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class TelegramUserStorageService {
    private final TelegramUserRepository telegramUserRepository;
}
