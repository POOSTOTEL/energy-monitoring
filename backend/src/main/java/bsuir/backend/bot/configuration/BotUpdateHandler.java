package bsuir.backend.bot.configuration;

import bsuir.backend.bot.service.KeyboardService;
import bsuir.backend.generator.service.storage.TelegramUserStorageService;
import bsuir.backend.generator.storage.entity.ReportAccessLog;
import bsuir.backend.generator.storage.entity.TelegramUser;
import bsuir.backend.generator.storage.repository.ReportAccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
public class BotUpdateHandler {

    private final Map<Long, String> reportTypeCache = new ConcurrentHashMap<>();
    private final Map<Long, Boolean> awaitingDateInput = new ConcurrentHashMap<>();

    private final TelegramUserStorageService userAccessService;
    private final KeyboardService keyboardService;
    private final ReportAccessLogRepository reportAccessLogRepository;
    private TelegramBotImpl telegramBot;

    public void handle(Update update) {
        try {
            if (update.hasMessage()) {
                handleMessage(update);
            } else if (update.hasCallbackQuery()) {
                handleCallback(update);
            }
        } catch (Exception e) {
            log.error("Error handling update", e);
        }
    }

    private void handleMessage(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        if (!userAccessService.isAuthorized(userId)) return;

        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        if ("/start".equals(text)) {
            reportTypeCache.remove(chatId);
            awaitingDateInput.remove(chatId);
            sendMainMenu(chatId);
        } else if (Boolean.TRUE.equals(awaitingDateInput.get(chatId))) {
            handleDateInput(chatId, text);
        }
    }

    private void handleCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        if ("REPORT_SELECT".equals(callbackData)) {
            sendReportTypeMenu(chatId);
        } else if (callbackData.startsWith("REPORT_TYPE:")) {
            String reportType = callbackData.split(":")[1];
            reportTypeCache.put(chatId, reportType); // Сохраняем тип отчета
            awaitingDateInput.put(chatId, true); // Устанавливаем флаг ожидания даты
            sendMessage(keyboardService.requestDateInput(chatId));
        }
    }

    private void sendMainMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Главное меню:");
        message.setReplyMarkup(keyboardService.getMainMenu());
        sendMessage(message);
    }

    private void sendReportTypeMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите тип отчета:");
        message.setReplyMarkup(keyboardService.getReportTypeMenu());
        sendMessage(message);
    }



    private void sendMessage(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
        }
    }

    private void handleDateInput(Long chatId, String dateInput) {
        if (isValidDate(dateInput)) {
            String reportType = reportTypeCache.get(chatId);
            String url = keyboardService.generateReportUrl(dateInput);
            sendReportWithDate(chatId, reportType, url);
            reportTypeCache.remove(chatId);
            awaitingDateInput.remove(chatId);
        } else {
            SendMessage errorMsg = new SendMessage();
            errorMsg.setChatId(chatId);
            errorMsg.setText("Неверный формат даты. Введите дату в формате yyyy-MM-dd:");
            sendMessage(errorMsg);
        }
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void sendReportWithDate(Long chatId, String reportType, String url) {
        try {
            TelegramUser user = userAccessService.findByTelegramId(chatId);

            ReportAccessLog logEntry = new ReportAccessLog();
            logEntry.setTelegramUser(user);
            logEntry.setReportName(reportType);
            logEntry.setParameters("{\"date\":\"" + url.split("date=")[1] + "\"}");
            logEntry.setAccessTime(LocalDateTime.now());

            reportAccessLogRepository.save(logEntry);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Отчет " + reportType + " готов:");

            InlineKeyboardButton openBtn = new InlineKeyboardButton("Открыть");
            openBtn.setUrl(url);
            message.setReplyMarkup(new InlineKeyboardMarkup(List.of(List.of(openBtn))));

            telegramBot.execute(message);
        } catch (Exception e) {
            log.error("Error generating report URL", e);
        }
    }

    public void setTelegramBot(TelegramBotImpl telegramBot) {
        this.telegramBot = telegramBot;
    }
}
