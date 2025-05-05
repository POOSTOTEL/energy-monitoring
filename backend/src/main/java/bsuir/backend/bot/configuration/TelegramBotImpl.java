package bsuir.backend.bot.configuration;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBotImpl extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final BotUpdateHandler botUpdateHandler;

    public TelegramBotImpl(String botUsername, String botToken, BotUpdateHandler botUpdateHandler) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.botUpdateHandler = botUpdateHandler;
    }


    @Override
    public void onUpdateReceived(Update update) {
        botUpdateHandler.handle(update);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
