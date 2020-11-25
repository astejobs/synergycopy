package com.synergy.EquipmentSearch;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EquipmentSearchResponse {

    @SerializedName("taskId")
    @Expose
    private Long taskId;
    @SerializedName("task_number")
    @Expose
    private String taskNumber;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("scheduleNumber")
    @Expose
    private String scheduleNumber;
    @SerializedName("briefDescription")
    @Expose
    private String briefDescription;
    @SerializedName("locationId")
    @Expose
    private Long locationId;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("buildingId")
    @Expose
    private Long buildingId;
    @SerializedName("buildingName")
    @Expose
    private String buildingName;
    @SerializedName("equipmentCode")
    @Expose
    private String equipmentCode;
    @SerializedName("scheduleDate")
    @Expose
    private Long scheduleDate;
    @SerializedName("completedBy")
    @Expose
    private String completedBy;
    @SerializedName("completedDate")
    @Expose
    private Long completedDate;
    @SerializedName("completedTime")
    @Expose
    private String completedTime;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("endDate")
    @Expose
    private Long endDate;
    @SerializedName("dueDate")
    @Expose
    private Long dueDate;

    String beforeImage;
    String afterImage;


    public EquipmentSearchResponse(Long taskId, String taskNumber, String status, String scheduleNumber, String briefDescription,
                                   Long locationId, String locationName, Long buildingId, String buildingName, String equipmentCode, Long scheduleDate,
                                   String completedBy, Long completedDate, String completedTime, String remarks, Long endDate,
                                   Long dueDate, String beforeImage, String afterImage) {
        this.taskId = taskId;
        this.taskNumber = taskNumber;
        this.status = status;
        this.scheduleNumber = scheduleNumber;
        this.briefDescription = briefDescription;
        this.locationId = locationId;
        this.locationName = locationName;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.equipmentCode = equipmentCode;
        this.scheduleDate = scheduleDate;
        this.completedBy = completedBy;
        this.completedDate = completedDate;
        this.completedTime = completedTime;
        this.remarks = remarks;
        this.endDate = endDate;
        this.dueDate = dueDate;
        this.beforeImage = beforeImage;
        this.afterImage = afterImage;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

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

    public String getScheduleNumber() {
        return scheduleNumber;
    }

    public void setScheduleNumber(String scheduleNumber) {
        this.scheduleNumber = scheduleNumber;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public Long getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Long scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public Long getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Long completedDate) {
        this.completedDate = completedDate;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }


    public String getBeforeImage() {
        return beforeImage;
    }

    public String getAfterImage() {
        return afterImage;
    }
}


