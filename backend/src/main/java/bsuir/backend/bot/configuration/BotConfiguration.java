package bsuir.backend.bot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

@Configuration
public class BotConfiguration {

    @Value("${spring.telegram.bot.name}")
    private String botUsername;

    @Value("${spring.telegram.bot.token}")
    private String botToken;

    @Bean
    public LongPollingBot telegramBot(BotUpdateHandler updateHandler){
        return new TelegramBotImpl(botUsername, botToken, updateHandler);
    }


}
