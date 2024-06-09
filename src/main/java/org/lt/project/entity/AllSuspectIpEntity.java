package org.lt.project.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="blocked_ips")
public class AllSuspectIpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "come_by")
    private String comeBy;
    @Column(name = "come_at")
    private Date comeAt;
    @Column(name="banned")
    private boolean banned;

    public AllSuspectIpEntity() {
    }

    public AllSuspectIpEntity(String ipAddress, String comeBy, Date comeAt, boolean banned) {
        this.ipAddress = ipAddress;
        this.comeBy = comeBy;
        this.comeAt = comeAt;
        this.banned = banned;
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

    public String getComeBy() {
        return comeBy;
    }

    public void setComeBy(String comeBy) {
        this.comeBy = comeBy;
    }

    public Date getComeAt() {
        return comeAt;
    }

    public void setComeAt(Date comeAt) {
        this.comeAt = comeAt;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
