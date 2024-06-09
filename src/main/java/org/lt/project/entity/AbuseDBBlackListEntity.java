package org.lt.project.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "abusedb_blacklists")
public class AbuseDBBlackListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String ipAddress;
    private Date lastReportedAt;
    private String countryCode;
    private boolean isBlocked;
    public long getId() {
        return id;
    }

    public AbuseDBBlackListEntity() {
    }

    public AbuseDBBlackListEntity(String ipAddress, Date lastReportedAt, String countryCode) {
        this.ipAddress = ipAddress;
        this.lastReportedAt = lastReportedAt;
        this.countryCode = countryCode;
        this.isBlocked=false;
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
    public Date getLastReportedAt() {
        return lastReportedAt;
    }
    public void setLastReportedAt(Date lastReportedAt) {
        this.lastReportedAt = lastReportedAt;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
