package com.synergy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUpdatePmTaskRequest {

    @SerializedName("task_number")
    @Expose
    private String taskNumber;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("compDate")
    @Expose
    private String compDate;
    @SerializedName("compTime")
    @Expose
    private String compTime;
    @SerializedName("completedBy")
    @Expose
    private String completedBy;
    String remarks;

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompDate() {
        return compDate;
    }

    public void setCompDate(String compDate) {
        this.compDate = compDate;
    }

    public String getCompTime() {
        return compTime;
    }

    public void setCompTime(String compTime) {
        this.compTime = compTime;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public GetUpdatePmTaskRequest(String taskNumber, String status, String compDate, String compTime, String completedBy, String remarks) {
        this.taskNumber = taskNumber;
        this.status = status;
        this.compDate = compDate;
        this.compTime = compTime;
        this.completedBy = completedBy;
        this.remarks = remarks;
    }
}
