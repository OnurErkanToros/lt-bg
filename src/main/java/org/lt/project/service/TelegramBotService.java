package org.lt.project.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBotService extends TelegramLongPollingBot  {

    public TelegramBotService() {
    super(new DefaultBotOptions(),"7946197257:AAFZYCNr24K5-s_osqyIfaU_LlWlfTuUNio");
    }

    @Override
    public void onUpdateReceived(Update update) {
        sendMessage(update.getMessage().getText());
    }

    @Override
    public String getBotUsername() {
        return "ltapp_log_bot";
    }

    public void sendMessage(String text){
        var message = SendMessage.builder().chatId("923246611").text(text).build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}

