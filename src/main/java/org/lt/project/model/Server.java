package org.lt.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "servers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String url;
    private String username;
    private String password;
    private int port;
    @Column(name = "remote_file_path")
    private String remoteFilePath;
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
