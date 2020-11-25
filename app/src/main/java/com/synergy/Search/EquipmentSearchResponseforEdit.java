package com.synergy.Search;

public class EquipmentSearchResponseforEdit {
    public EquipmentSearchResponseforEdit(String equipmentCode,
                                          String name, int id,
                                          String equipmentType) {

        this.equipmentCode=equipmentCode;
        this.name = name;
        this.id = id;
        this.equipmentType = equipmentType;
    }


    private String equipmentCode;
    private String name;
    private int id;
    private String equipmentType;


    public String getEquipmentCode() {
        return equipmentCode;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public String getEquipmentType() {
        return equipmentType;
    }



}
