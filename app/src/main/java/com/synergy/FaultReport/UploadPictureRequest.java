package com.synergy.FaultReport;

public class UploadPictureRequest {
    private String frId;
    private StringBuilder data;

    public UploadPictureRequest(String mfrId, StringBuilder mimage) {
        this.frId = mfrId;
        this.data = mimage;
    }
}
