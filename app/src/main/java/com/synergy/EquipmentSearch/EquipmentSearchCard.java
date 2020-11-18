package com.synergy.EquipmentSearch;

public class EquipmentSearchCard {
    int taskId;
    String task_number;
    String workspace;

    public EquipmentSearchCard(int taskId, String task_number, String workspace) {
        this.taskId = taskId;
        this.task_number = task_number;
        this.workspace = workspace;
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
}
