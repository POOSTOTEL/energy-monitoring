package bsuir.backend.bot.configuration;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class TelegramBotImpl extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;
    private final BotUpdateHandler botUpdateHandler;

    public TelegramBotImpl(
            String botUsername,
            String botToken,
            BotUpdateHandler botUpdateHandler
    ) {
        super(new DefaultBotOptions(), botToken);
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.botUpdateHandler = botUpdateHandler;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        botUpdateHandler.handle(update);
    }
}