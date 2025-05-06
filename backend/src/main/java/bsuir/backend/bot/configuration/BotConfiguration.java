package bsuir.backend.bot.configuration;

import bsuir.backend.bot.service.KeyboardService;
import bsuir.backend.generator.service.storage.TelegramUserStorageService;
import bsuir.backend.generator.storage.repository.ReportAccessLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

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
    public TelegramBotsApi telegramBotsApi(TelegramBotImpl telegramBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(telegramBot);
        return botsApi;
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