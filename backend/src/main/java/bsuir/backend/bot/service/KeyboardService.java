package bsuir.backend.bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    @Value("${report.server.base-url}")
    private String serverBaseUrl;

    public InlineKeyboardMarkup getMainMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        InlineKeyboardButton reportBtn = new InlineKeyboardButton("📊 Выбрать отчет");
        reportBtn.setCallbackData("REPORT_SELECT");

        keyboard.add(List.of(reportBtn));
        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getReportTypeMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // Первый ряд кнопок
        InlineKeyboardButton anomaliesBtn = new InlineKeyboardButton("Аномалии");
        anomaliesBtn.setCallbackData("REPORT_TYPE:ANOMALIES");

        InlineKeyboardButton consumptionBtn = new InlineKeyboardButton("Расход за период");
        consumptionBtn.setCallbackData("REPORT_TYPE:CONSUMPTION");

        // Второй ряд кнопок
        InlineKeyboardButton graphBtn = new InlineKeyboardButton("График потребления");
        graphBtn.setCallbackData("REPORT_TYPE:GRAPH");

        InlineKeyboardButton heatmapBtn = new InlineKeyboardButton("Тепловая карта");
        heatmapBtn.setCallbackData("REPORT_TYPE:HEATMAP");

        // Третий ряд кнопок
        InlineKeyboardButton forecastBtn = new InlineKeyboardButton("Прогнозирование");
        forecastBtn.setCallbackData("REPORT_TYPE:FORECAST");

        InlineKeyboardButton statusBtn = new InlineKeyboardButton("Статусы активности");
        statusBtn.setCallbackData("REPORT_TYPE:STATUS");

        keyboard.add(List.of(anomaliesBtn, consumptionBtn));
        keyboard.add(List.of(graphBtn, heatmapBtn));
        keyboard.add(List.of(forecastBtn, statusBtn));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getPeriodMenu(String reportType) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        InlineKeyboardButton dayBtn = new InlineKeyboardButton("День");
        dayBtn.setCallbackData("REPORT_PERIOD:" + reportType + ":DAY");

        InlineKeyboardButton weekBtn = new InlineKeyboardButton("Неделя");
        weekBtn.setCallbackData("REPORT_PERIOD:" + reportType + ":WEEK");

        InlineKeyboardButton monthBtn = new InlineKeyboardButton("Месяц");
        monthBtn.setCallbackData("REPORT_PERIOD:" + reportType + ":MONTH");

        keyboard.add(List.of(dayBtn, weekBtn, monthBtn));
        return new InlineKeyboardMarkup(keyboard);
    }

    public String generateReportUrl(String reportType, String period) {
        return serverBaseUrl + "/reports/" + reportType.toLowerCase() + "?period=" + period.toLowerCase();
    }
}
