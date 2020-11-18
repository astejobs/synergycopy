package com.synergy.Otp;

public class OtpRequest {
    public OtpRequest(String username, String code, String deviceToken) {
        this.username = username;
        this.code = code;
        this.deviceToken = deviceToken;
    }

    String username,code,deviceToken;
}
