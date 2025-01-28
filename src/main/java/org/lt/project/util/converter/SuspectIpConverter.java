package org.lt.project.util.converter;

import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.model.SuspectIP;

public class SuspectIpConverter {
    public static SuspectIP convert(SuspectIpRequestDto suspectIpRequestDto) {
    return SuspectIP.builder()
        .ipAddress(suspectIpRequestDto.ip())
        .retry(suspectIpRequestDto.retry())
        .status(suspectIpRequestDto.status())
        .statusAt(suspectIpRequestDto.statusAt())
        .statusBy(suspectIpRequestDto.statusBy())
        .line(suspectIpRequestDto.line())
        .pattern(suspectIpRequestDto.pattern())
        .host(suspectIpRequestDto.host())
        .createdAt(suspectIpRequestDto.createdAt())
        .build();
    }

    public static SuspectIpResponseDto convert(SuspectIP suspectIP) {
        return SuspectIpResponseDto.builder()
                .id(suspectIP.getId())
                .ip(suspectIP.getIpAddress())
                .pattern(suspectIP.getPattern())
                .host(suspectIP.getHost())
                .line(suspectIP.getLine())
                .retry(suspectIP.getRetry())
                .createdAt(suspectIP.getCreatedAt())
                .status(suspectIP.getStatus())
                .statusBy(suspectIP.getStatusBy())
                .statusAt(suspectIP.getStatusAt())
                .build();
    }
}
