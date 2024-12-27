package org.lt.project.dto;

import lombok.Builder;
import org.lt.project.model.SuspectIP;

import java.util.Date;

@Builder
public record SuspectIpResponseDto(
        String ip,
        String host,
        String line,
        int retry,
        String pattern,
        Date createdAt,
        SuspectIP.IpStatus status,
        Date statusAt,
        String statusBy
) {
}
