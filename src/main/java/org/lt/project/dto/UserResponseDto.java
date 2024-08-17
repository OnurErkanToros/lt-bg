package org.lt.project.dto;

import lombok.Builder;

@Builder
public record UserResponseDto(String username, String token) {
}
