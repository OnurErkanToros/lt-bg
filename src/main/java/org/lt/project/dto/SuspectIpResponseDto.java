package org.lt.project.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record SuspectIpResponseDto(
        String ip,
        String host,
        String line,
        int accessForbiddenNumber,
        boolean isBanned,
        String pattern,
        Date createdAt,
        Date banDate,
        String banBy
) {
}
