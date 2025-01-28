package org.lt.project.util.converter;

import org.lt.project.dto.LogListenerRegexRequestDto;
import org.lt.project.dto.LogListenerRegexResponseDto;
import org.lt.project.model.LogListenerRegex;
import org.lt.project.service.UserService;

import java.util.Date;

public class LogListenerRegexConverter {
    public static LogListenerRegexResponseDto convert(LogListenerRegex entity) {
        return LogListenerRegexResponseDto.builder()
                .id(entity.getId())
                .pattern(entity.getPattern())
                .active(entity.isActive())
                .explanation(entity.getExplanation())
                .creUser(entity.getCreatedBy())
                .creDate(entity.getCreatedAt())
                .build();
    }

    public static LogListenerRegex convert(LogListenerRegexRequestDto logListenerRegexRequestDto) {
        return LogListenerRegex.builder()
                .pattern(logListenerRegexRequestDto.pattern())
                .active(true)
                .explanation(logListenerRegexRequestDto.explanation())
                .createdBy(UserService.getAuthenticatedUser())
                .createdAt(new Date())
                .build();
    }
}
