package org.lt.project.dto;

import lombok.Builder;
import org.lt.project.model.Settings;

@Builder
public record SettingsRequestDto (
        Settings.SettingType settingType,
        Settings.VariableType variableType,
        String title,
        String description,
        String key,
        String value
){
}
