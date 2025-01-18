package org.lt.project.util.convertor;

import java.util.Date;
import org.lt.project.dto.ServerRequestDto;
import org.lt.project.dto.ServerResponseDto;
import org.lt.project.model.Server;
import org.lt.project.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class ServerConverter {
    public static Server convert(ServerRequestDto serverRequestDto) {
    return Server.builder()
        .name(serverRequestDto.name())
        .host(serverRequestDto.host())
        .username(serverRequestDto.username())
        .password(serverRequestDto.password())
        .port(serverRequestDto.port())
        .createdBy(UserService.getAuthenticatedUser())
        .createdAt(new Date())
        .fileName(serverRequestDto.fileName())
        .isActive(serverRequestDto.isActive())
        .isSFTP(serverRequestDto.isSFTP())
        .remoteFilePath(serverRequestDto.remoteFilePath())
        .build();
    }

    public static ServerResponseDto convert(Server server) {
    return ServerResponseDto.builder()
        .id(server.getId())
        .isSFTP(server.isSFTP())
        .name(server.getName())
        .host(server.getHost())
        .username(server.getUsername())
        .password(server.getPassword())
        .port(server.getPort())
        .remoteFilePath(server.getRemoteFilePath())
        .fileName(server.getFileName())
        .createdBy(server.getCreatedBy())
        .createdAt(server.getCreatedAt())
        .isActive(server.isActive())
        .build();
    }
}
