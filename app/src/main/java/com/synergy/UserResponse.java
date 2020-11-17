package com.synergy;

public class UserResponse {



    private String role;
    private String token;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public UserResponse(String role, String token,String user) {
        this.user=user;
        this.role = role;
        this.token = token;
    }
}
