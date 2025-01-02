package org.lt.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "suspect_ips")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuspectIP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "ip_address")
    private String ipAddress;
    private String host;
    private int retry;
    private String line;
    private Date createdAt;
    private String createdBy;
    private String pattern;
    @Enumerated(EnumType.STRING)
    private IpStatus status;
    private Date statusAt;
    private String statusBy;

    public enum IpStatus {
        NEW,
        BANNED
    }
}
