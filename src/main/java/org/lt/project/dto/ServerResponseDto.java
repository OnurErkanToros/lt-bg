package org.lt.project.dto;


import lombok.Builder;

import java.util.Date;

@Builder
public record ServerResponseDto(
        int id,
        String name,
        String host,
        String username,
        String password,
        int port,
        String remoteFilePath,
        String fileName,
        String createdBy,
        Date createdAt,
        boolean isActive,
        boolean isSFTP
) {

}
