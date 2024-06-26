package org.lt.project.dto;

    
import java.util.Date;

public class ServerResponseDto {
    private int id;
    private String name;
    private String url;
    private String username;
    private String password;
    private int port;
    private String remoteFilePath;
    private String fileName;
    private String createdBy;
    private Date createdAt;
    private boolean active;

    public ServerResponseDto(int id,String name, String url, String username, String password, int port, String remoteFilePath, String fileName, String createdBy, Date createdAt, boolean active) {
        this.id=id;
        this.name = name;
        this.url = url;
        this.username = username;
        this.password = password;
        this.port = port;
        this.remoteFilePath = remoteFilePath;
        this.fileName = fileName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.active = active;
    }

    public ServerResponseDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
