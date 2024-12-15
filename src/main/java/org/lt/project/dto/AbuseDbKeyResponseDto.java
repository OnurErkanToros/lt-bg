package org.lt.project.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record AbuseDbKeyResponseDto(
        long id,
        String abuseKey,
        Date createdAt,
        String createdBy,
        boolean isActive
) {
}
