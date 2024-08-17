package org.lt.project.dto;

import lombok.Builder;

@Builder
public record AllSuspectIpRequestDto(String ipAddress) {
}
