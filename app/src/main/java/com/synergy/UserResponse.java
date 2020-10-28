package com.synergy;

public class UserResponse {



    private String role;
    private String token;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponse(String role, String token) {
        this.role = role;
        this.token = token;
    }
}
