package org.lt.project.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "abusedb_check_logs")
public class AbuseDBLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "is_public")
    private boolean isPublic;
    @Column(name = "ip_version")
    private int ipVersion;
    @Column(name = "is_white_listed")
    private boolean isWhiteListed;
    @Column(name = "abuse_confidence_score")
    private int abuseConfidenceScore;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "usageType")
    private String usageType;
    private String isp;
    private String domain;
    @Column(name = "is_tor")
    private String isTor;
    @Column(name = "total_reports")
    private int totalReports;
    @Column(name = "num_distinct_users")
    private int numDistinctUsers;
    @Column(name = "last_reported_at")
    private Date lastReportedAt;
    @Column(name = "check_date")
    private Date checkDate;

    public AbuseDBLogEntity() {
    }

    public AbuseDBLogEntity(String ipAddress, boolean isPublic, int ipVersion, boolean isWhiteListed, int abuseConfidenceScore, String countryCode, String countryName, String usageType, String isp, String domain, String isTor, int totalReports, int numDistinctUsers, Date lastReportedAt) {
        this.ipAddress = ipAddress;
        this.isPublic = isPublic;
        this.ipVersion = ipVersion;
        this.isWhiteListed = isWhiteListed;
        this.abuseConfidenceScore = abuseConfidenceScore;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.usageType = usageType;
        this.isp = isp;
        this.domain = domain;
        this.isTor = isTor;
        this.totalReports = totalReports;
        this.numDistinctUsers = numDistinctUsers;
        this.lastReportedAt = lastReportedAt;
        this.checkDate=new Date();
    }

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
    public boolean isPublic() {
        return isPublic;
    }
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    public int getIpVersion() {
        return ipVersion;
    }
    public void setIpVersion(int ipVersion) {
        this.ipVersion = ipVersion;
    }
    public boolean isWhiteListed() {
        return isWhiteListed;
    }
    public void setWhiteListed(boolean isWhiteListed) {
        this.isWhiteListed = isWhiteListed;
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

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
}