package bsuir.backend.generator.storage.repository;

import bsuir.backend.generator.storage.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser,Long> {
    Optional<TelegramUser> findByTelegramId(Long telegramId);
    boolean existsByTelegramId(Long telegramId);
}
