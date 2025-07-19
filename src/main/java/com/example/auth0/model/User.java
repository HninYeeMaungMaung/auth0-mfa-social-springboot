package com.example.auth0.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String role;
    private String email;
    private String contact;
    private String address;
    private String company;

    public User() {}

    public User(Long id, String username, String password, String role, String email, String contact, String address, String company) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.company = company;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getContact() { return contact; }
    public String getAddress() { return address; }
    public String getCompany() { return company; }

    // Setters (optional)
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }
    public void setContact(String contact) { this.contact = contact; }
    public void setAddress(String address) { this.address = address; }
    public void setCompany(String company) { this.company = company; }
}
