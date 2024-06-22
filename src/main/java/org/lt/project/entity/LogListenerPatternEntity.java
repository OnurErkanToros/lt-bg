package org.lt.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "log_listener_patterns")
public class LogListenerPatternEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String pattern;
    private String explanation;
    @Column(name = "cre_user")
    private String creUser;
    @Column(name = "cre_date")
    private Date creDate;

    public LogListenerPatternEntity(String pattern, String explanation, String creUser) {
        this.pattern = pattern;
        this.explanation = explanation;
        this.creUser = creUser;
        this.creDate = new Date();
    }

    public LogListenerPatternEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getCreUser() {
        return creUser;
    }

    public void setCreUser(String creUser) {
        this.creUser = creUser;
    }

    public Date getCreDate() {
        return creDate;
    }

    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }
}
