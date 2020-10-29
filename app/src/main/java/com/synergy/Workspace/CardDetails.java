package com.synergy.Workspace;

public class CardDetails {
    public CardDetails(String buildingDescription, String workspaceId) {
        this.buildingDescription = buildingDescription;
        this.workspaceId = workspaceId;
    }

    public String getBuildingDescription() {
        return buildingDescription;
    }

    public void setBuildingDescription(String buildingDescription) {
        this.buildingDescription = buildingDescription;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    String buildingDescription, workspaceId;

}
