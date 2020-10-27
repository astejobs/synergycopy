package com.synergy.faultReport;

public class CreateFaultResponse {
    String frId;

    public CreateFaultResponse(String frId) {
        this.frId = frId;
    }

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }
}
