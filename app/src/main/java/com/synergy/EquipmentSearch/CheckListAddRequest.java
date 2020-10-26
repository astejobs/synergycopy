package com.synergy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckListAddRequest {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("taskId")
    @Expose
    private Integer taskId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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

    public CheckListAddRequest(Integer id, Integer taskId, String status, String remarks) {
        this.id = id;
        this.taskId = taskId;
        this.status = status;
        this.remarks = remarks;
    }
}
