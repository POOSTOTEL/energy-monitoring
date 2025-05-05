package bsuir.backend.generator.service.storage;

import bsuir.backend.generator.storage.entity.TelegramUser;
import bsuir.backend.generator.storage.repository.TelegramUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class TelegramUserStorageService {
    private final TelegramUserRepository telegramUserRepository;

    public boolean isAuthorized(Long telegramId) {
        return telegramUserRepository.existsByTelegramId(telegramId);
    }

    public TelegramUser getOrCreateUser(Long telegramId, String username, String fullName) {
        return telegramUserRepository.findByTelegramId(telegramId)
                .orElseGet(() -> {
                    TelegramUser newUser = new TelegramUser();
                    newUser.setTelegramId(telegramId);
                    newUser.setUsername(username);
                    newUser.setFullName(fullName);
                    return telegramUserRepository.save(newUser);
                });
    }

    public TelegramUser findByTelegramId(Long telegramId) {
        return telegramUserRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
