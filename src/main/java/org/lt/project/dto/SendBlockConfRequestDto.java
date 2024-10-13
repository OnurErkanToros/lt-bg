package org.lt.project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SendBlockConfRequestDto(
        @NotEmpty List<@NotNull ServerRequestDto> serverList,
        @NotEmpty List<@NotNull BanRequestDto> ipForBan
) {
}
