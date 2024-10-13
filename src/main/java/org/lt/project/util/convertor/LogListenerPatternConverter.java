package org.lt.project.util.convertor;

import org.lt.project.dto.LogListenerPatternRequestDto;
import org.lt.project.dto.LogListenerPatternResponseDto;
import org.lt.project.model.LogListenerPattern;
import org.lt.project.service.UserService;

import java.util.Date;

public class LogListenerPatternConverter {
    public static LogListenerPatternResponseDto convert(LogListenerPattern entity) {
        return LogListenerPatternResponseDto.builder()
                .id(entity.getId())
                .pattern(entity.getPattern())
                .explanation(entity.getExplanation())
                .creUser(entity.getPattern())
                .creDate(entity.getCreatedAt())
                .build();
    }

    public static LogListenerPattern convert(LogListenerPatternRequestDto logListenerPatternRequestDto) {
        return LogListenerPattern.builder()
                .pattern(logListenerPatternRequestDto.pattern())
                .explanation(logListenerPatternRequestDto.explanation())
                .createdBy(UserService.getAuthenticatedUser())
                .createdAt(new Date())
                .build();
    }
}
