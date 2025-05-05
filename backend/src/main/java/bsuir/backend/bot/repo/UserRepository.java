package bsuir.backend.bot.repo;

import bsuir.backend.bot.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<TelegramUser, Long> {
    Optional<TelegramUser> findByTelegramId(Long telegramId); // FIXED: Возвращает Optional
    boolean existsByTelegramId(Long telegramId);
}