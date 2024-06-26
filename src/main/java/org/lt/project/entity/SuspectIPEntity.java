package org.lt.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "suspect_ips")
public class SuspectIPEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "which_host")
    private String whichHost;
    @Column(name = "access_forbidden_number")
    private int accessForbiddenNumber;
    @Column
    private String line;

    public SuspectIPEntity() {
    }

    public SuspectIPEntity(String ipAddress, String whichHost, int accessForbiddenNumber,String line) {
        this.ipAddress = ipAddress;
        this.whichHost = whichHost;
        this.accessForbiddenNumber = accessForbiddenNumber;
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
    public String getWhichHost() {
        return whichHost;
    }
    public void setWhichHost(String whichHost) {
        this.whichHost = whichHost;
    }
    public int getAccessForbiddenNumber() {
        return accessForbiddenNumber;
    }
    public void setAccessForbiddenNumber(int accessForbiddenNumber) {
        this.accessForbiddenNumber = accessForbiddenNumber;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
