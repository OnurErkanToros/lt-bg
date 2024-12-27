package org.lt.project.util.convertor;

import org.lt.project.dto.SettingsRequestDto;
import org.lt.project.dto.SettingsResponseDto;
import org.lt.project.model.Settings;
import org.lt.project.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SettingsConverter {
    public static Settings convert(SettingsRequestDto settingsRequestDto){
        String authenticatedUser=UserService.getAuthenticatedUser();
        return Settings
                .builder()
                .settingType(settingsRequestDto.settingType())
                .variableType(settingsRequestDto.variableType())
                .title(settingsRequestDto.title())
                .description(settingsRequestDto.description())
                .settingKey(settingsRequestDto.key())
                .value(settingsRequestDto.value())
                .createdAt(new Date())
                .updatedAt(new Date())
                .createdBy(authenticatedUser)
                .updatedBy(authenticatedUser)
                .build();
    }

    public static SettingsResponseDto convert(Settings settings){
        return SettingsResponseDto
                .builder()
                .title(settings.getTitle())
                .key(settings.getSettingKey())
                .value(settings.getValue())
                .settingType(settings.getSettingType())
                .description(settings.getDescription())
                .updatedBy(settings.getUpdatedBy())
                .updatedAt(settings.getUpdatedAt())
                .createdAt(settings.getCreatedAt())
                .createdBy(settings.getCreatedBy())
                .build();
    }
}
