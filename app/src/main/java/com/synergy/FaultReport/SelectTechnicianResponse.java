package com.synergy.FaultReport;

public class SelectTechnicianResponse {
    int id;
    String name;
    String username;

    public SelectTechnicianResponse() {
    }

    public SelectTechnicianResponse(int id, String name, String equipmentCode) {
        this.id = id;
        this.name = name;
        this.username = equipmentCode;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
