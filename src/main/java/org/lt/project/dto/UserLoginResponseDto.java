package org.lt.project.dto;

import lombok.Builder;

@Builder
public record UserLoginResponseDto(String username, String token) {
}