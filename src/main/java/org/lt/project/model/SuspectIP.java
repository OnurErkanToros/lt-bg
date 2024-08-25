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
    @Column(name = "access_forbidden_number")
    private int accessForbiddenNumber;
    @Column
    private String line;
    private Date createdAt;
    private String createdBy;
    private boolean banned;
    private Date banDate;
    private String banBy;
    private String pattern;
}
