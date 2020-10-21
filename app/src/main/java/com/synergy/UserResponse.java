package com.synergy;

public class UserResponse {



    private String buildingDescription;
    private String workspaceId;

    public String getBuildingDescription ()
    {
        return buildingDescription;
    }

    public void setBuildingDescription (String buildingDescription)
    {
        this.buildingDescription = buildingDescription;
    }

    public String getWorkspaceId ()
    {
        return workspaceId;
    }

    public void setWorkspaceId (String workspaceId)
    {
        this.workspaceId = workspaceId;
    }

}
