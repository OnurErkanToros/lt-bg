package org.lt.project.dto;

import lombok.Builder;

@Builder
public record LogListenerRegexRequestDto(String pattern,
                                         String explanation) {
}
