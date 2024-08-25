package org.lt.project.core.convertor;

import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.model.SuspectIP;
import org.lt.project.service.UserService;

public class SuspectIpConverter {
    public static SuspectIP convert(SuspectIpRequestDto suspectIpRequestDto) {
        return SuspectIP.builder()
                .ipAddress(suspectIpRequestDto.ip())
                .accessForbiddenNumber(suspectIpRequestDto.accessForbiddenNumber())
                .pattern(suspectIpRequestDto.pattern())
                .line(suspectIpRequestDto.line())
                .host(suspectIpRequestDto.host())
                .createdAt(suspectIpRequestDto.createdAt())
                .createdBy(UserService.getAuthenticatedUser())
                .isBanned(suspectIpRequestDto.isBanned())
                .build();
    }

    public static SuspectIpResponseDto convert(SuspectIP suspectIP) {
        return SuspectIpResponseDto.builder()
                .ip(suspectIP.getIpAddress())
                .accessForbiddenNumber(suspectIP.getAccessForbiddenNumber())
                .pattern(suspectIP.getPattern())
                .line(suspectIP.getLine())
                .host(suspectIP.getHost())
                .createdAt(suspectIP.getCreatedAt())
                .isBanned(suspectIP.isBanned())
                .banDate(suspectIP.getBanDate())
                .banBy(suspectIP.getBanBy())
                .build();
    }
}
