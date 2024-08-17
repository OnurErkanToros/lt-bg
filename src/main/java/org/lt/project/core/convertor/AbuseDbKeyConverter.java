package org.lt.project.core.convertor;

import org.lt.project.dto.AbuseDbKeyRequestDto;
import org.lt.project.dto.AbuseDbKeyResponseDto;
import org.lt.project.model.AbuseDBKey;
import org.lt.project.service.UserService;

import java.util.Date;


public class AbuseDbKeyConverter {
    public static AbuseDBKey convert(AbuseDbKeyRequestDto abuseDbKeyRequestDto) {
        return AbuseDBKey.builder()
                .abuseKey(abuseDbKeyRequestDto.abuseKey())
                .createdAt(new Date())
                .isActive(true)
                .createdBy(UserService.getAuthenticatedUser())
                .build();
    }

    public static AbuseDbKeyResponseDto convert(AbuseDBKey abuseDBKey) {
        return AbuseDbKeyResponseDto.builder()
                .abuseKey(abuseDBKey.getAbuseKey())
                .isActive(abuseDBKey.isActive())
                .createdAt(abuseDBKey.getCreatedAt())
                .createdBy(abuseDBKey.getCreatedBy())
                .build();
    }
}
