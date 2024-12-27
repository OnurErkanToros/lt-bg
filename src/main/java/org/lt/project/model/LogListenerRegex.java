package org.lt.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "log_listener_regex")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogListenerRegex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String pattern;
    private String explanation;
    private boolean active;
    private Date createdAt;
    private String createdBy;
}
