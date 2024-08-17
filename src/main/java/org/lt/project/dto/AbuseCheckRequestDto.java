package org.lt.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AbuseCheckRequestDto(
        @NotNull int maxAgeInDays,
        @NotBlank String ipAddress
) {
}
