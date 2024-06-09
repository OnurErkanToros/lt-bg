package org.lt.project.dto;

import java.util.Date;

public class AbuseDbKeyResponseDto {
    private String abuseKey;
    private Date createdAt;
    private boolean isActive;

    public AbuseDbKeyResponseDto(String abuseKey, boolean isActive) {
        this.abuseKey = abuseKey;
        this.createdAt = new Date();
        this.isActive = isActive;
    }

    public AbuseDbKeyResponseDto() {
    }

    public String getAbuseKey() {
        return abuseKey;
    }

    public void setAbuseKey(String abuseKey) {
        this.abuseKey = abuseKey;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
