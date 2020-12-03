package com.synergy;

public class UserResponse {



    private String role;
    private String token;
    private String username;

    public String getUsername() {
        return username;
    }

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

    public UserResponse(String role, String token,String username) {
        this.username =username;
        this.role = role;
        this.token = token;
    }
}
