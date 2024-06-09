package org.lt.project.core.convertor;

import org.lt.project.dto.LogListenerPatternRequestDto;
import org.lt.project.dto.LogListenerPatternResponseDto;
import org.lt.project.entity.LogListenerPatternEntity;

public class LogListenerPatternConverter {
    public static LogListenerPatternResponseDto convert(LogListenerPatternEntity entity){
        return new LogListenerPatternResponseDto(entity.getId(),
                entity.getPattern(),
                entity.getExplanation(),
                entity.getCreUser(),
                entity.getCreDate());
    }

    public static LogListenerPatternEntity convert(LogListenerPatternRequestDto logListenerPatternRequestDto, String creUser) {
        return new LogListenerPatternEntity(logListenerPatternRequestDto.getPattern(), logListenerPatternRequestDto.getExplanation(), creUser);
    }
}
