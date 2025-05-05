package bsuir.backend.bot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "telegram_users")
@Data
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telegram_user_id")
    private Long telegramUserId;

    @Column(name = "telegram_id", nullable = false, unique = true)
    private Long telegramId;

    @Column(name = "username")
    private String username;

    @Column(name = "full_name")
    private String fullName;


}