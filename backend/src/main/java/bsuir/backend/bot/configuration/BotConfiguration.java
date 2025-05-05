package bsuir.backend.bot.configuration;

import bsuir.backend.bot.service.KeyboardService;
import bsuir.backend.generator.service.storage.TelegramUserStorageService;
import bsuir.backend.generator.storage.repository.ReportAccessLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

    @Bean
    public TelegramBotImpl telegramBot(
            @Value("${spring.telegram.bot.name}") String botUsername,
            @Value("${spring.telegram.bot.token}") String botToken,
            BotUpdateHandler botUpdateHandler
    ) {
        TelegramBotImpl bot = new TelegramBotImpl(botUsername, botToken, botUpdateHandler);
        botUpdateHandler.setTelegramBot(bot);
        return bot;
    }

    @Bean
    public BotUpdateHandler botUpdateHandler(
            TelegramUserStorageService userAccessService,
            KeyboardService keyboardService,
            ReportAccessLogRepository reportAccessLogRepository
    ) {
        return new BotUpdateHandler(userAccessService, keyboardService, reportAccessLogRepository);
    }

}
