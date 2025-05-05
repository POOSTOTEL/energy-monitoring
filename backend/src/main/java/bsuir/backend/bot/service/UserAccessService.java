package bsuir.backend.bot.service;

import bsuir.backend.bot.model.TelegramUser;
import bsuir.backend.bot.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAccessService {
    private final UserRepository userRepository;

    public UserAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isAuthorized(Long telegramId) {
        return userRepository.existsByTelegramId(telegramId);
    }

    public TelegramUser getOrCreateUser(Long telegramId, String username, String fullName) {
        return userRepository.findByTelegramId(telegramId)
                .orElseGet(() -> {
                    TelegramUser newUser = new TelegramUser();
                    newUser.setTelegramId(telegramId);
                    newUser.setUsername(username);
                    newUser.setFullName(fullName);
                    return userRepository.save(newUser);
                });
    }

    public TelegramUser findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}