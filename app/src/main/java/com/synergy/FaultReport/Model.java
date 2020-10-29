package com.synergy.FaultReport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model {
    @SerializedName("frId")
    @Expose
    String frId;

    public String getFrId() {
        return frId;
    }

    public Model(String frId) {
        this.frId = frId;
    }
}
