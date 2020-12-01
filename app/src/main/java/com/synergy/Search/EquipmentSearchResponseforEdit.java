package com.synergy.Search;

public class EquipmentSearchResponseforEdit {
    public String getAssetSubType() {
        return assetSubType;
    }

    public void setAssetSubType(String assetSubType) {
        this.assetSubType = assetSubType;
    }

    public EquipmentSearchResponseforEdit(String equipmentCode,
                                          String name, int id,
                                          String equipmentType, String equipmentSubType, String locationName, String buildingName) {
        this.buildingName = buildingName;
        this.locationName = locationName;
        this.assetSubType =equipmentSubType;
        this.equipmentCode = equipmentCode;
        this.name = name;
        this.id = id;
        this.assetType = equipmentType;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    private String equipmentCode;
    private String name, locationName, buildingName, assetSubType;
    private int id;
    private String assetType;


    public String getEquipmentCode() {
        return equipmentCode;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAssetType() {
        return assetType;
    }


}
