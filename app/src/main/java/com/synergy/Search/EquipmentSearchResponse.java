package com.synergy.Search;

public class EquipmentSearchResponse {
    int id;
    String name;
    String equipmentCode;

    public EquipmentSearchResponse() {
    }

    public EquipmentSearchResponse(int id, String name, String equipmentCode) {
        this.id = id;
        this.name = name;
        this.equipmentCode = equipmentCode;
    }

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

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }
}
