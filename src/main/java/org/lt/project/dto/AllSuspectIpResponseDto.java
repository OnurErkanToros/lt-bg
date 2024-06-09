package org.lt.project.dto;

import java.util.Date;

public class AllSuspectIpResponseDto {
    private long id;
    private String ipAddress;
    private String comeBy;
    private Date comeAt;
    private boolean banned;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getComeBy() {
        return comeBy;
    }

    public void setComeBy(String comeBy) {
        this.comeBy = comeBy;
    }

    public Date getComeAt() {
        return comeAt;
    }

    public void setComeAt(Date comeAt) {
        this.comeAt = comeAt;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
