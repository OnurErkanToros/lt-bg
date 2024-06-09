package org.lt.project.dto;

import java.util.Date;

public class AbuseBlackListResponseDto {
    private String ipAddress;
    private int abuseConfidenceScore;
    private Date lastReportedAt;
    private String countryCode;
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public int getAbuseConfidenceScore() {
        return abuseConfidenceScore;
    }
    public void setAbuseConfidenceScore(int abuseConfidenceScore) {
        this.abuseConfidenceScore = abuseConfidenceScore;
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
    
    
}
