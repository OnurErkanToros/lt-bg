package org.lt.project.dto;

import jakarta.validation.constraints.NotNull;

public class AbuseCheckRequestDto {
    @NotNull
    private int maxAgeInDays;
    private String ipAddress;
    
    public AbuseCheckRequestDto(int maxAgeInDays, String ipAddress) {
        this.maxAgeInDays = maxAgeInDays;
        this.ipAddress = ipAddress;
    }

    public AbuseCheckRequestDto() {
    }

    public int getMaxAgeInDays() {
        return maxAgeInDays;
    }
    public void setMaxAgeInDays(int maxAgeInDays) {
        this.maxAgeInDays = maxAgeInDays;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
