package org.lt.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private SettingType settingType;
    private VariableType variableType;
    private String title;
    private String description;
    private String settingKey;
    private String value;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;

    public enum SettingType{
        UI,
        LOG_LISTENER
    }
    public enum VariableType{
        STRING,
        BOOLEAN,
        INTEGER,
        DOUBLE,
    }
}
