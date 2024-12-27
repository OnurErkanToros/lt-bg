package org.lt.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "abusedb_check_logs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AbuseDBCheckLog {
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
    private String checkBy;
    @Enumerated(EnumType.STRING)
    private SuspectIP.IpStatus status;
    private Date statusAt;
    private String statusBy;
}