package org.lt.project.core.convertor;

import org.lt.project.dto.ServerRequestDto;
import org.lt.project.dto.ServerResponseDto;
import org.lt.project.entity.ServerEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ServerConverter {
    public static ServerEntity convert(ServerRequestDto serverRequestDto, String createdBy) {
        return new ServerEntity(
                serverRequestDto.getName(),
                serverRequestDto.getUrl(),
                serverRequestDto.getUsername(),
                serverRequestDto.getPassword(),
                serverRequestDto.getPort(),
                serverRequestDto.getRemoteFilePath(),
                serverRequestDto.getFileName(),
                new Date(),
                createdBy,
                serverRequestDto.isActive()
                );
    }

    public static ServerResponseDto convert(ServerEntity serverEntity) {
        return new ServerResponseDto(
                serverEntity.getId(),
                serverEntity.getName(),
                serverEntity.getUrl(),
                serverEntity.getUsername(),
                serverEntity.getPassword(),
                serverEntity.getPort(),
                serverEntity.getRemoteFilePath(),
                serverEntity.getFileName(),
                serverEntity.getCreatedBy(),
                serverEntity.getCreatedAt(),
                serverEntity.isActive()
        );
    }
}
