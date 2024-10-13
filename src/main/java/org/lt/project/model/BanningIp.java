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
public class BanningIp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String ip;
    @Enumerated(EnumType.STRING)
    private BanningIpType ipType;
    @Enumerated(EnumType.STRING)
    private BanningIpStatus status;
    private Date transferredAt;
    private String transferredBy;
    private Date createdAt;
    private String createdBy;

    public enum BanningIpStatus {
        TRANSFERRED,
        NOT_TRANSFERRED,
        ERROR,
    }

    public enum BanningIpType {
        BLACKLIST,
        CHECK,
        LISTENER,
        MANUEL
    }
}