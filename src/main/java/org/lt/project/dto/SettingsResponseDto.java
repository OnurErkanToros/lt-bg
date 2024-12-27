package org.lt.project.dto;

import lombok.Builder;
import org.lt.project.model.Settings;

import java.util.Date;

@Builder
public record SettingsResponseDto (
        Settings.SettingType settingType,
        String title,
        String description,
        String key,
        String value,
        Date createdAt,
        Date updatedAt,
        String createdBy,
        String updatedBy
){}
