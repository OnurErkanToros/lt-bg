package org.lt.project.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record AllSuspectIpResponseDto(
        long id,
        String ipAddress,
        String comeBy,
        Date comeAt,
        boolean banned
) {
}
