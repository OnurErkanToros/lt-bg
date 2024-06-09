package org.lt.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(value = { "hostnames","reports"})
public class AbuseCheckResponseDto{
    private String ipAddress;
    private int ipVersion;
    private boolean isPublic;
    private boolean isWhitelisted;
    private int abuseConfidenceScore;
    private String countryCode;
    private String countryName;
    private String usageType;
    private String isp;
    private String domain;
    private String isTor;
    private int totalReports;
    private int numDistinctUsers;
    private Date lastReportedAt;

    public boolean getIsPublic() {
        return isPublic;
    }
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public int getIpVersion() {
        return ipVersion;
    }
    public void setIpVersion(int ipVersion) {
        this.ipVersion = ipVersion;
    }
    public boolean getIsWhitelisted() {
        return isWhitelisted;
    }
    public void setIsWhitelisted(boolean isWhitelisted) {
        this.isWhitelisted = isWhitelisted;
    }
    public int getAbuseConfidenceScore() {
        return abuseConfidenceScore;
    }
    public void setAbuseConfidenceScore(int abuseConfidenceScore) {
        this.abuseConfidenceScore = abuseConfidenceScore;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public String getUsageType() {
        return usageType;
    }
    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }
    public String getIsp() {
        return isp;
    }
    public void setIsp(String isp) {
        this.isp = isp;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getIsTor() {
        return isTor;
    }
    public void setIsTor(String isTor) {
        this.isTor = isTor;
    }
    public int getTotalReports() {
        return totalReports;
    }
    public void setTotalReports(int totalReports) {
        this.totalReports = totalReports;
    }
    public int getNumDistinctUsers() {
        return numDistinctUsers;
    }
    public void setNumDistinctUsers(int numDistinctUsers) {
        this.numDistinctUsers = numDistinctUsers;
    }
    public Date getLastReportedAt() {
        return lastReportedAt;
    }
    public void setLastReportedAt(Date lastReportedAt) {
        this.lastReportedAt = lastReportedAt;
    }

    
}
