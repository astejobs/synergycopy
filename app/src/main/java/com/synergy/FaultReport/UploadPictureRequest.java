package com.synergy.FaultReport;

public class UploadPictureRequest {

    private String frId;
    private StringBuilder image;

    public UploadPictureRequest(String frId, StringBuilder image) {
        this.frId = frId;
        this.image = image;
    }

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public StringBuilder  getImage() {
        return image;
    }

}
