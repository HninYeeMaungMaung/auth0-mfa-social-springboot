package com.example.auth0.model;

import jakarta.persistence.*;
import lombok.*;
@Table(name = "userdevice")
@Entity
public class UserDevice {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private String deviceId;

    public UserDevice() {}

    public UserDevice(Long id, Long userId, String deviceId) {
        this.id = id;
        this.userId = userId;
        this.deviceId = deviceId;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getDeviceId() { return deviceId; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
}