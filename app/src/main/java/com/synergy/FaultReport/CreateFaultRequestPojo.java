package com.synergy.FaultReport;

import java.util.ArrayList;

public class CreateFaultRequestPojo {

    String requestorName;
    String requestorContactNo, faultCategoryDesc, locationDesc;
    Location location;
    Building building;
    MaintGrp maintGrp;
    Equipment equipment;
    Division division;
    Department department;
    Priority priority;
    FaultCategory faultCategory;
    private ArrayList<AttendedBy> attendedBy;


    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }


    public String getRequestorContactNo() {
        return requestorContactNo;
    }

    public void setRequestorContactNo(String requestorContactNo) {
        this.requestorContactNo = requestorContactNo;
    }

    public String getFaultCategoryName() {
        return faultCategoryDesc;
    }

    public void setFaultCategoryName(String faultCategoryDesc) {
        this.faultCategoryDesc = faultCategoryDesc;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
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

    public MaintGrp getMaintGrp() {
        return maintGrp;
    }

    public void setMaintGrp(MaintGrp maintGrp) {
        this.maintGrp = maintGrp;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public FaultCategory getFaultCategory() {
        return faultCategory;
    }

    public void setFaultCategory(FaultCategory faultCategory) {
        this.faultCategory = faultCategory;
    }


    public CreateFaultRequestPojo(String requestorName, Building building, Location location,
                                  String requestorContactNo,
                                  Priority priority, MaintGrp maintGrp, FaultCategory faultCategory, Department department,
                                  String faultCategoryDesc, String locationDesc, Division division, Equipment equipment,
                                  ArrayList<AttendedBy> attendedBy) {
        this.division = division;
        this.attendedBy=attendedBy;
        this.faultCategoryDesc = faultCategoryDesc;
        this.locationDesc = locationDesc;
        this.requestorName = requestorName;
        this.building = building;
        this.location = location;
        this.requestorContactNo = requestorContactNo;
        this.priority = priority;
        this.maintGrp = maintGrp;
        this.faultCategory = faultCategory;
        this.department = department;
        this.equipment = equipment;
    }

    @Override
    public String toString() {
        return "ClassPojo [ faultCategory = " + faultCategory + ", maintGrp = " + maintGrp + ", priority = " + priority + "," +
                " building = " + building + ", faultCategoryName = " + faultCategoryDesc + ", division = " + division + " locationDesc = " + locationDesc + ", location = " + location + "," +
                " requestorName = " + requestorName + ", department = " + department + " ]";
    }
}

class Division {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + "]";
    }
}


class Department {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + "]";
    }
}

class Priority {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + "]";
    }
}

class FaultCategory {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + "]";
    }
}

class MaintGrp {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + "]";
    }
}

class Building {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + "]";
    }
}

class Location {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + "]";
    }

}

class Equipment {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class AttendedBy {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

