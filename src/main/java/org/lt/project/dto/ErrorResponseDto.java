package org.lt.project.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        int statusCode,
        String timeStamp
) {
    public ErrorResponseDto(String message, int statusCode) {
        this(message, statusCode, LocalDateTime.now().toString());
    }
}
