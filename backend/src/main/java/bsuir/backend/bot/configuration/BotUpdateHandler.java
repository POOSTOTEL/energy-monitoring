package bsuir.backend.bot.configuration;

import bsuir.backend.bot.service.KeyboardService;
import bsuir.backend.bot.service.UserAccessService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotUpdateHandler {

    private final UserAccessService userAccessService;
    private final KeyboardService keyboardService;

    public BotUpdateHandler(UserAccessService userAccessService, KeyboardService keyboardService){
        this.userAccessService = userAccessService;
        this.keyboardService = keyboardService;
    }

    public void handle(Update update) {
        if(update.hasMessage()){
            Long thId = update.getMessage().getFrom().getId();
            if(!userAccessService.isAuthorized(thId)){
                return;
            }
            String text = update.getMessage().getText();
            if ("/start".equals(text)) {
                // Приветствие и кнопки
            }
        }
    }


}
