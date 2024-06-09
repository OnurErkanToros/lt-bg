package org.lt.project.core.convertor;

import org.lt.project.dto.AbuseDbKeyRequestDto;
import org.lt.project.dto.AbuseDbKeyResponseDto;
import org.lt.project.entity.AbuseDBKeyEntity;
import org.springframework.stereotype.Component;

@Component
public class AbuseDbKeyConverter {
    public static AbuseDBKeyEntity convert(AbuseDbKeyRequestDto abuseDbKeyRequestDto) {
        return new AbuseDBKeyEntity(abuseDbKeyRequestDto.getAbuseKey());
    }

    public static AbuseDbKeyResponseDto convert(AbuseDBKeyEntity abuseDBKeyEntity) {
        return new AbuseDbKeyResponseDto(abuseDBKeyEntity.getAbuseKey(), abuseDBKeyEntity.isActive());
    }
}
