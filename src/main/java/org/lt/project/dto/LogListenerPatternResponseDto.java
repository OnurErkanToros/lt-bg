package org.lt.project.dto;

import java.util.Date;

public class LogListenerPatternResponseDto {
    private int id;
    private String pattern;
    private String explanation;
    private String creUser;
    private Date creDate;

    public LogListenerPatternResponseDto(int id, String pattern, String explanation, String creUser, Date creDate) {
        this.id = id;
        this.pattern = pattern;
        this.explanation = explanation;
        this.creUser = creUser;
        this.creDate = creDate;
    }

    public LogListenerPatternResponseDto() {
    }

    public int getId() {
        return id;
    }

    public String getPattern() {
        return pattern;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getCreUser() {
        return creUser;
    }

    public Date getCreDate() {
        return creDate;
    }
}
