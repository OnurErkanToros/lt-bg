package org.lt.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "abusedb_blacklist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbuseDBBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String ipAddress;
    private Date lastReportedAt;
    private String countryCode;
    private int abuseConfidenceScore;
    private Date createdAt;
    private String createdBy;
    @Enumerated(EnumType.STRING)
    private SuspectIP.IpStatus status;
    private Date statusAt;
    private String statusBy;
}
