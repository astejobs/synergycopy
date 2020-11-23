package com.synergy.FaultReport;

public class UploadPictureRequest {
    private String id;
    private StringBuilder data;

    public UploadPictureRequest(String mfrId, StringBuilder mimage) {
        this.id = mfrId;
        this.data = mimage;
    }
}
