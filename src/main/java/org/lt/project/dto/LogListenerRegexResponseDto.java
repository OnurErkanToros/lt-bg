package org.lt.project.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record LogListenerRegexResponseDto(
        int id,
        String pattern,
        String explanation,
        boolean active,
        String creUser,
        Date creDate
) {
}
