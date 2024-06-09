package org.lt.project.dto;

import java.util.Date;

public class AuthenticationResponseDto {
    private String username;
    private Date lastLoginDate;
    private String token;

    public AuthenticationResponseDto(String username, Date lastLoginDate, String token) {
        this.username = username;
        this.lastLoginDate = lastLoginDate;
        this.token = token;
    }

    public AuthenticationResponseDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
