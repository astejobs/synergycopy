package com.synergy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCheckListResponse /*{
    int id;
    ChecklistProperty checklistProperty;
    String status;
    String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChecklistProperty getChecklistProperty() {
        return checklistProperty;
    }

    public void setChecklistProperty(ChecklistProperty checklistProperty) {
        this.checklistProperty = checklistProperty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public GetCheckListResponse(int id, ChecklistProperty checklistProperty, String status, String remarks) {
        this.id = id;
        this.checklistProperty = checklistProperty;
        this.status = status;
        this.remarks = remarks;
    }

    public static class ChecklistProperty{
        String description;
        String descriptionType;

        public ChecklistProperty(String description, String descriptionType) {
            this.description = description;
            this.descriptionType = descriptionType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescriptionType() {
            return descriptionType;
        }

        public void setDescriptionType(String descriptionType) {
            this.descriptionType = descriptionType;
        }
    }


}*/
{
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