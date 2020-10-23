package com.synergy.EquipmentSearch;

import android.os.Parcel;
import android.os.Parcelable;

public class EquipmentSearchResponse {
    String equipmentCode;
    String equipmentType;
    String name;
    Location location;
    Building building;
    String assetNo;

    public EquipmentSearchResponse(String equipmentCode, String equipmentType, String name, Location location, Building building, String assetNo) {
        this.equipmentCode = equipmentCode;
        this.equipmentType = equipmentType;
        this.name = name;
        this.location = location;
        this.building = building;
        this.assetNo = assetNo;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }
}

class Location {
    String name;
    int id;

    public Location(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class Workspace {
    int id;
    String workspaceId;
    String description;
    String buildingDescription;
    String owner;
    String contractor;
    String bldngOwnerPayAmt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuildingDescription() {
        return buildingDescription;
    }

    public void setBuildingDescription(String buildingDescription) {
        this.buildingDescription = buildingDescription;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getBldngOwnerPayAmt() {
        return bldngOwnerPayAmt;
    }

    public void setBldngOwnerPayAmt(String bldngOwnerPayAmt) {
        this.bldngOwnerPayAmt = bldngOwnerPayAmt;
    }

    public Workspace(int id, String workspaceId, String description,
                     String buildingDescription, String owner,
                     String contractor, String bldngOwnerPayAmt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.description = description;
        this.buildingDescription = buildingDescription;
        this.owner = owner;
        this.contractor = contractor;
        this.bldngOwnerPayAmt = bldngOwnerPayAmt;
    }
}

class Building {
    int id;
    String name;
    Workspace workspace;
    String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Building(int id, String name, Workspace workspace, String description) {
        this.id = id;
        this.name = name;
        this.workspace = workspace;
        this.description = description;
    }
}