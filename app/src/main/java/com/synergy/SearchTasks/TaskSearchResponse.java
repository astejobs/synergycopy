package com.synergy.SearchTasks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskSearchResponse {


    @SerializedName("acknowledgementTime")
    @Expose
    private String acknowledgementTime;
    @SerializedName("briefDescription")
    @Expose
    private String briefDescription;
    @SerializedName("buildingId")
    @Expose
    private Long buildingId;
    @SerializedName("buildingName")
    @Expose
    private String buildingName;
    @SerializedName("completedBy")
    @Expose
    private String completedBy;
    @SerializedName("completedDate")
    @Expose
    private String completedDate;
    @SerializedName("completedTime")
    @Expose
    private String completedTime;
    @SerializedName("dueDate")
    @Expose
    private String dueDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("equipmentCode")
    @Expose
    private String equipmentCode;
    @SerializedName("equipmentName")
    @Expose
    private String equipmentName;
    @SerializedName("locationId")
    @Expose
    private Long locationId;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("scheduleDate")
    @Expose
    private String scheduleDate;
    @SerializedName("scheduleNumber")
    @Expose
    private String scheduleNumber;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("taskId")
    @Expose
    private int taskId;
    @SerializedName("task_number")
    @Expose
    private String taskNumber;

    private String beforeImage;
    private String afterImage;

    public TaskSearchResponse(String acknowledgementTime, String briefDescription, Long buildingId, String buildingName,
                              String completedBy, String completedDate, String completedTime, String dueDate, String endDate,
                              String equipmentCode, String equipmentName, Long locationId, String locationName, String remarks,
                              String scheduleDate, String scheduleNumber, String status, int taskId, String taskNumber, String beforeImage, String afterImage) {
        this.acknowledgementTime = acknowledgementTime;
        this.briefDescription = briefDescription;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.completedBy = completedBy;
        this.completedDate = completedDate;
        this.completedTime = completedTime;
        this.dueDate = dueDate;
        this.endDate = endDate;
        this.equipmentCode = equipmentCode;
        this.equipmentName = equipmentName;
        this.locationId = locationId;
        this.locationName = locationName;
        this.remarks = remarks;
        this.scheduleDate = scheduleDate;
        this.scheduleNumber = scheduleNumber;
        this.status = status;
        this.taskId = taskId;
        this.taskNumber = taskNumber;
        this.beforeImage = beforeImage;
        this.afterImage = afterImage;
    }

    public String getAcknowledgementTime() {
        return acknowledgementTime;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public String getScheduleNumber() {
        return scheduleNumber;
    }

    public String getStatus() {
        return status;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public String getBeforeImage() {
        return beforeImage;
    }

    public String getAfterImage() {
        return afterImage;
    }
}
