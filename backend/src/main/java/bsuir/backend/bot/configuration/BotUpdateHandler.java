package bsuir.backend.bot.configuration;

import bsuir.backend.bot.model.ReportAccessLog;
import bsuir.backend.bot.model.TelegramUser;
import bsuir.backend.bot.repo.ReportAccessLogRepository;
import bsuir.backend.bot.service.KeyboardService;
import bsuir.backend.bot.service.UserAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BotUpdateHandler {
    private static final Logger log = LoggerFactory.getLogger(BotUpdateHandler.class);

    private final UserAccessService userAccessService;
    private final KeyboardService keyboardService;
    private final TelegramBotImpl telegramBot;
    private final ReportAccessLogRepository reportAccessLogRepository;

    public BotUpdateHandler(UserAccessService userAccessService,
                            KeyboardService keyboardService,
                            TelegramBotImpl telegramBot,
                            ReportAccessLogRepository reportAccessLogRepository) {
        this.userAccessService = userAccessService;
        this.keyboardService = keyboardService;
        this.telegramBot = telegramBot;
        this.reportAccessLogRepository = reportAccessLogRepository;
    }

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

        String text = update.getMessage().getText();
        if ("/start".equals(text)) {
            sendMainMenu(update.getMessage().getChatId());
        }
    }

    private void handleCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (callbackData.startsWith("REPORT_SELECT")) {
            sendReportTypeMenu(chatId);
        }
        else if (callbackData.startsWith("REPORT_TYPE:")) {
            String reportType = callbackData.split(":")[1];
            sendPeriodMenu(chatId, reportType);
        }
        else if (callbackData.startsWith("REPORT_PERIOD:")) {
            String[] parts = callbackData.split(":");
            String reportType = parts[1];
            String period = parts[2];
            sendReportUrl(chatId, reportType, period);
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

    private void sendPeriodMenu(Long chatId, String reportType) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите период:");
        message.setReplyMarkup(keyboardService.getPeriodMenu(reportType));
        sendMessage(message);
    }

    private void sendReportUrl(Long chatId, String reportType, String period) {
        try {
            String url = keyboardService.generateReportUrl(reportType, period);

            TelegramUser user = userAccessService.findByTelegramId(chatId);


            ReportAccessLog logEntry = new ReportAccessLog();
            logEntry.setUser(user);
            logEntry.setReportName(reportType);
            logEntry.setParameters("{\"period\":\"" + period + "\"}");
            logEntry.setAccessTime(LocalDateTime.now());
            logEntry.setSuccess(true);


            reportAccessLogRepository.save(logEntry);


            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Отчет готов: " + url);


            InlineKeyboardButton openBtn = new InlineKeyboardButton("Открыть");
            openBtn.setUrl(url);
            message.setReplyMarkup(new InlineKeyboardMarkup(List.of(List.of(openBtn))));

            telegramBot.execute(message);
        } catch (Exception e) {
            log.error("Error generating report URL", e);
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
        }
    }



}
