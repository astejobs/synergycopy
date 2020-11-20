package com.synergy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPmTaskItemsResponse {
    int taskId;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @SerializedName("task_number")
    @Expose
    private String taskNumber;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("pmTaskNo")
    @Expose
    private String pmTaskNo;
    @SerializedName("scheduleNumber")
    @Expose
    private String pmScheduleNo;
    @SerializedName("briefDescription")
    @Expose
    private String briefDescription;
    @SerializedName("equipmentCode")
    @Expose
    private String equipmentCode;
    @SerializedName("locationName")
    @Expose
    private String equipmentLocation;
    @SerializedName("buildingName")
    @Expose
    private String equipmentBuilding;
    @SerializedName("scheduleDate")
    @Expose
    private Long scheduleDate;
    @SerializedName("compDate")
    @Expose
    private Long compDate;
    @SerializedName("compTime")
    @Expose
    private Long compTime;
    @SerializedName("dueDate")
    @Expose
    private Long dueDate;
    @SerializedName("endDate")
    @Expose
    private Long endDate;
    @SerializedName("completedBy")
    @Expose
    private String completedBy;
    @SerializedName("status")
    @Expose
    private String status;

    public GetPmTaskItemsResponse(String taskNumber, String remarks, String pmTaskNo, String pmScheduleNo, String briefDescription,
                                  String equipmentCode, String equipmentLocation, String equipmentBuilding, Long scheduleDate,
                                  Long compDate, Long compTime, Long dueDate, Long endDate, String completedBy, String status, int taskId) {
        this.taskNumber = taskNumber;
        this.remarks = remarks;
        this.pmTaskNo = pmTaskNo;
        this.pmScheduleNo = pmScheduleNo;
        this.briefDescription = briefDescription;
        this.equipmentCode = equipmentCode;
        this.equipmentLocation = equipmentLocation;
        this.equipmentBuilding = equipmentBuilding;
        this.scheduleDate = scheduleDate;
        this.compDate = compDate;
        this.compTime = compTime;
        this.dueDate = dueDate;
        this.endDate = endDate;
        this.completedBy = completedBy;
        this.status = status;
        this.taskId = taskId;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPmTaskNo() {
        return pmTaskNo;
    }

    public void setPmTaskNo(String pmTaskNo) {
        this.pmTaskNo = pmTaskNo;
    }

    public String getPmScheduleNo() {
        return pmScheduleNo;
    }

    public void setPmScheduleNo(String pmScheduleNo) {
        this.pmScheduleNo = pmScheduleNo;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentLocation() {
        return equipmentLocation;
    }

    public void setEquipmentLocation(String equipmentLocation) {
        this.equipmentLocation = equipmentLocation;
    }

    public String getEquipmentBuilding() {
        return equipmentBuilding;
    }

    public void setEquipmentBuilding(String equipmentBuilding) {
        this.equipmentBuilding = equipmentBuilding;
    }

    public Long getScheduleDate() {
        return scheduleDate;
    }

    public Long getCompDate() {
        return compDate;
    }

    public Long getCompTime() {
        return compTime;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}



