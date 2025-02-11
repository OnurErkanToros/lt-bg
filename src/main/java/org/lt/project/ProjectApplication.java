package org.lt.project;

import lombok.RequiredArgsConstructor;
import org.lt.project.service.TelegramBotService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@RequiredArgsConstructor
public class ProjectApplication {
    private final TelegramBotService telegramBotService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

}
