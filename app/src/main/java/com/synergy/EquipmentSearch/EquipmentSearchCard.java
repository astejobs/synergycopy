package com.synergy.EquipmentSearch;

public class EquipmentSearchCard {
    int taskId;
    String task_number;
    String workspace;
    String status, buildingName, locationName;
    long scheduleDate;
    String afterImage;
    String beforeImage;
    String source;

    public EquipmentSearchCard(int taskId, String task_number, String workspace,
                               String status, String buildingName, String locationName, long scheduleDate,
                               String afterImage, String beforeImage, String source) {
        this.taskId = taskId;
        this.task_number = task_number;
        this.workspace = workspace;
        this.status = status;
        this.buildingName = buildingName;
        this.locationName = locationName;
        this.scheduleDate = scheduleDate;
        this.afterImage = afterImage;
        this.beforeImage = beforeImage;
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getLocationName() {
        return locationName;
    }

    public long getScheduleDate() {
        return scheduleDate;
    }

    public String getWorkspace() {
        return workspace;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTask_number() {
        return task_number;
    }

    public void setTask_number(String task_number) {
        this.task_number = task_number;
    }

    public String getAfterImage() {
        return afterImage;
    }

    public String getBeforeImage() {
        return beforeImage;
    }

    public String getSource() {
        return source;
    }
}
