package org.lt.project.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record LogListenerPatternResponseDto(
        int id,
        String pattern,
        String explanation,
        String creUser,
        Date creDate
) {
}
