package org.lt.project.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ServerRequestDto(
        @NotBlank String name,
        @NotBlank String url,
        @NotBlank String username,
        @NotBlank String password,
        @NotNull int port,
        @NotBlank String remoteFilePath,
        @NotBlank String fileName,
        boolean isActive
) {
}
