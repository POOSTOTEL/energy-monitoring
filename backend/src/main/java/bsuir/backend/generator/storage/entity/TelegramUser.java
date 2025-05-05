package bsuir.backend.generator.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Table(schema = "public", name = "telegram_users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser {

    /**
     * Генерируемый идентификатор пользователя Telegram в БД
     */
    @Id
    @Column(name = "telegram_user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Идентификатор пользователя Telegram
     */
    @Column(name = "telegram_id")
    private Long telegramId;

    /**
     * Telegram тег пользователя
     */
    @Column(name = "username")
    private String username;

    /**
     * Telegram имя пользователя
     */
    @Column(name = "full_name")
    private String fullName;

}
