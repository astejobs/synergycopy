package com.synergy;

public class GetCheckListResponse {
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


}