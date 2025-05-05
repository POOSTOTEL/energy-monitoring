package bsuir.backend.bot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.telegrambots.meta.api.objects.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByTelegramId(Long telegramId);
}
