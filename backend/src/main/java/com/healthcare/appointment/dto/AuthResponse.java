package com.healthcare.appointment.dto;

public class AuthResponse {

    private String token;
    private String role;
    private Long userId;
    private String fullName;

    public AuthResponse(String token, String role, Long userId, String fullName) {
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }
}

