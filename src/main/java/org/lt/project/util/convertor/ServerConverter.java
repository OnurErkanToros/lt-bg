package org.lt.project.util.convertor;

import org.lt.project.dto.ServerRequestDto;
import org.lt.project.dto.ServerResponseDto;
import org.lt.project.model.Server;
import org.lt.project.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ServerConverter {
    public static Server convert(ServerRequestDto serverRequestDto) {
        return Server.builder()
                .name(serverRequestDto.name())
                .url(serverRequestDto.url())
                .password(serverRequestDto.password())
                .port(serverRequestDto.port())
                .createdBy(UserService.getAuthenticatedUser())
                .createdAt(new Date())
                .fileName(serverRequestDto.fileName())
                .isActive(serverRequestDto.isActive())
                .remoteFilePath(serverRequestDto.remoteFilePath())
                .build();
    }

    public static ServerResponseDto convert(Server server) {
        return new ServerResponseDto(
                server.getId(),
                server.getName(),
                server.getUrl(),
                server.getUsername(),
                server.getPassword(),
                server.getPort(),
                server.getRemoteFilePath(),
                server.getFileName(),
                server.getCreatedBy(),
                server.getCreatedAt(),
                server.isActive()
        );
    }
}
