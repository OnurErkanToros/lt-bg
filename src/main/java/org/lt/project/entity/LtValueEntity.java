package org.lt.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lt_values")
public class LtValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;
    private String myKey;
    private String myValue;
    
    public void setId(long id) {
        this.id = id;
    }
    public String getMyKey() {
        return myKey;
    }
    public void setMyKey(String myKey) {
        this.myKey = myKey;
    }
    public String getMyValue() {
        return myValue;
    }
    public void setMyValue(String myValue) {
        this.myValue = myValue;
    }
    
}
