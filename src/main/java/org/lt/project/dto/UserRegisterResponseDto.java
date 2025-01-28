package org.lt.project.dto;

import lombok.Builder;

@Builder
public record UserRegisterResponseDto(
    String username,
    String token,
    String message
) {
} 