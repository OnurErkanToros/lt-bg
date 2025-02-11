package org.lt.project.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lt.project.service.AuthenticationService;
import org.lt.project.service.TelegramBotService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramConfig {
    private final AuthenticationService authenticationService;
    @Bean
    public TelegramBotService telegramBotService() {
        TelegramBotService telegramBotService = new TelegramBotService(authenticationService);
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotService);
        } catch (TelegramApiException e) {
            log.error("Error while registering bot", e);
        }
        return telegramBotService;
    }
}
