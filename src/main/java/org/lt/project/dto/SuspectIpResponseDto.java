package org.lt.project.dto;

import lombok.Builder;
import org.lt.project.model.IpStatus;

import java.util.Date;

@Builder
public record SuspectIpResponseDto(
        String ip,
        String host,
        String line,
        int accessForbiddenNumber,
        String pattern,
        Date createdAt,
        IpStatus status,
        Date statusAt,
        String statusBy
) {
}
