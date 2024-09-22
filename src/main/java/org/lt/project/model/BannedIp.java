package org.lt.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "banned_ip")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannedIp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String ip;
    @Enumerated(EnumType.STRING)
    private BannedIpType ipType;
    private boolean transferred = false;
    private Date transferredAt;
    private String transferredBy;
    private Date createdAt;
    private String createdBy;
}
