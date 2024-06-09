package org.lt.project.dto;

public class SuspectIpDto {
    private Integer id;
    private String ip;
    private String whichHost;
    private String line;
    private int accessForbiddenNumber;
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getWhichHost() {
        return whichHost;
    }

    public void setWhichHost(String whichHost) {
        this.whichHost = whichHost;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAccessForbiddenNumber() {
        return accessForbiddenNumber;
    }

    public void setAccessForbiddenNumber(int accessForbiddenNumber) {
        this.accessForbiddenNumber = accessForbiddenNumber;
    }
}
