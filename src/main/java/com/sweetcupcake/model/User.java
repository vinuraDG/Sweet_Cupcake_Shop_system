package com.sweetcupcake.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private Role role;
    private String createdAt;

    public enum Role {
        CASHIER, MANAGER
    }

    public User() {}

    public User(String username, String password, String fullName, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public boolean isManager() { return role == Role.MANAGER; }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', fullName='%s', role=%s}", id, username, fullName, role);
    }
}
