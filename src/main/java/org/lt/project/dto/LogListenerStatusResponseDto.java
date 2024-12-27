package org.lt.project.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public record LogListenerStatusResponseDto(
        @Enumerated(EnumType.STRING)
        LogListenerStatus status,
        String startTime,
        String endTime
) {
    public enum LogListenerStatus {
        STOPPED,
        STARTED
    }
}
