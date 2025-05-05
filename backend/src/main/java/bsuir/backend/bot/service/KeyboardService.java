package bsuir.backend.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    public InlineKeyboardMarkup getMainMenu() {
        InlineKeyboardMarkup btn = new InlineKeyboardMarkup("Открыть отчёт");
        btn.setCallbackData("get_report");
        List<List<InlineKeyboardButton>> rows = List.of(List.of(btn));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }


}
