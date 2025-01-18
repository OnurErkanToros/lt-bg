package org.lt.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank(message = "Kullanıcı adı zorunludur")
    private String username;

    @NotBlank(message = "Şifre zorunludur")
    private String password;
}