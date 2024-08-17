package org.lt.project.dto;

import lombok.Builder;
import org.lt.project.model.Role;

import java.util.Set;

@Builder
public record UserRequestDto(
        String name,
        String username,
        String password,
        Set<Role> authorities
) {
}