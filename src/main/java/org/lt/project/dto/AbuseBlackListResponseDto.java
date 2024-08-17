package org.lt.project.dto;

import java.util.Date;

public record AbuseBlackListResponseDto(
        String ipAddress,
        int abuseConfidenceScore,
        Date lastReportedAt,
        String countryCode) {
}
