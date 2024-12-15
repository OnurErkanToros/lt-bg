package org.lt.project.dto;

import lombok.Builder;


public record UserLoginResponseDto(String username, String token) {
    @Builder public UserLoginResponseDto{}
}
