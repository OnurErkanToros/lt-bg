package org.lt.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(value = { "hostnames","reports"})
public record AbuseCheckResponseDto(
        String ipAddress,
        int ipVersion,
        boolean isPublic,
        boolean isWhitelisted,
        int abuseConfidenceScore,
        String countryCode,
        String countryName,
        String usageType,
        String isp,
        String domain,
        String isTor,
        int totalReports,
        int numDistinctUsers,
        Date lastReportedAt) {

}
