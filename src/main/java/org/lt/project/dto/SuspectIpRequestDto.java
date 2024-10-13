package org.lt.project.dto;


import lombok.Builder;

import java.util.Date;

@Builder
public record SuspectIpRequestDto(
        String ip,
        String host,
        String line,
        int accessForbiddenNumber,
        String pattern,
        Date createdAt
) {
}
