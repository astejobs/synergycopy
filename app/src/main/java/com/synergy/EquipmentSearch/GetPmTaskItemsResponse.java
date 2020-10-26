package com.synergy.EquipmentSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPmTaskItemsResponse {
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
    private double scheduleDate;
    @SerializedName("compDate")
    @Expose
    private double compDate;
    @SerializedName("compTime")
    @Expose
    private double compTime;
    @SerializedName("dueDate")
    @Expose
    private double dueDate;
    @SerializedName("endDate")
    @Expose
    private double endDate;
    @SerializedName("completedBy")
    @Expose
    private String completedBy;
    @SerializedName("status")
    @Expose
    private String status;

    public GetPmTaskItemsResponse(String taskNumber, String remarks, String pmTaskNo, String pmScheduleNo, String briefDescription,
                                  String equipmentCode, String equipmentLocation, String equipmentBuilding, double scheduleDate,
                                  double compDate, double compTime, double dueDate, double endDate, String completedBy, String status) {
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

    public double getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(double scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public double getCompDate() {
        return compDate;
    }

    public void setCompDate(double compDate) {
        this.compDate = compDate;
    }

    public double getCompTime() {
        return compTime;
    }

    public void setCompTime(double compTime) {
        this.compTime = compTime;
    }

    public double getDueDate() {
        return dueDate;
    }

    public void setDueDate(double dueDate) {
        this.dueDate = dueDate;
    }

    public double getEndDate() {
        return endDate;
    }

    public void setEndDate(double endDate) {
        this.endDate = endDate;
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



