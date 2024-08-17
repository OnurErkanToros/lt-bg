package org.lt.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "abusedb_keys")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbuseDBKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String abuseKey;
    private Date createdAt;
    private String createdBy;
    private boolean isActive;
}
