package org.lt.project.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "abusedb_keys")
public class AbuseDBKeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String abuseKey;
    private Date createdAt;
    private boolean isActive;

    public AbuseDBKeyEntity() {

    }
    public AbuseDBKeyEntity(String abuseKey) {
        this.abuseKey = abuseKey;
        this.createdAt = new Date();
        this.isActive = true;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getAbuseKey() {
        return abuseKey;
    }
    public void setAbuseKey(String abuseKey) {
        this.abuseKey = abuseKey;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    
}
