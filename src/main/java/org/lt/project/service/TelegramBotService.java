package org.lt.project.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.RegisterRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class TelegramBotService extends TelegramLongPollingBot  {
    private final AuthenticationService authenticationService;
    private final Map<String, String> userSteps = new HashMap<>();
    private String currentStep = "";
    private final long chatId = 923246611; // Sabit chatId
    private String username;

    public TelegramBotService(AuthenticationService authenticationService) {
    super(new DefaultBotOptions(),"7946197257:AAFZYCNr24K5-s_osqyIfaU_LlWlfTuUNio");
        this.authenticationService = authenticationService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            String messageText = update.getMessage().getText();


            if(messageText.equals("/register") && update.getMessage().getChatId() == chatId) {
                currentStep = "USERNAME"; // Kullanıcı adı alma adımına başla
                sendMessage("Lütfen kullanıcı adınızı girin:");
            }
            else if("USERNAME".equals(currentStep) && update.getMessage().getChatId() == chatId) {
                username = messageText;
                if(authenticationService.isValidUsername(username)) {
                    currentStep = "PASSWORD";
                    sendMessage("Kullanıcı adı doğru. Lütfen şifrenizi girin:");
                } else {
                    sendMessage("Geçersiz kullanıcı adı. Lütfen tekrar deneyin:");
                }
            }
            else if("PASSWORD".equals(currentStep) && update.getMessage().getChatId() == chatId) {
                if(authenticationService.isValidPassword(messageText)) {
                    authenticationService.registerUser(RegisterRequest.builder().username(username).password(messageText).build());
                    sendMessage("Kayıt işleminiz başarıyla tamamlandı.");
                    currentStep = "";
                } else {
                    sendMessage("Geçersiz şifre. Lütfen tekrar deneyin:");
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "ltapp_log_bot";
    }

    public void sendMessage(String text){
        var message = SendMessage.builder().chatId(chatId).text(text).build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}

