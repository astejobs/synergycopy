package com.synergy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCheckListResponse {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("descriptionType")
    @Expose
    private String descriptionType;
    @SerializedName("blanks")
    @Expose
    private List<String> blanks = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public GetCheckListResponse(int id, String description, String descriptionType, List<String> blanks, String status, String remarks) {
        this.id = id;
        this.description = description;
        this.descriptionType = descriptionType;
        this.blanks = blanks;
        this.status = status;
        this.remarks = remarks;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public List<String> getBlanks() {
        return blanks;
    }

    public String getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }
}