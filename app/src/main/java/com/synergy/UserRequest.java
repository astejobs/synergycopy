package com.synergy;

public class UserRequest {

    String username;
    String password;
    String deviceToken;
    //String deviceToken;

    public UserRequest(String username, String password,String deviceToken) {
        this.deviceToken=deviceToken;
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    /*public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
*/
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
