package com.synergy.search;

import java.nio.charset.Charset;

public class SearchResponse {
    String frId, clientFrId, customerRefId, requestorName, requestorContactNo, location,
            building, division, locationDesc, faultCategory, faultCategoryName, priority,
            department, maintGrp, status, reportedTime, equipment, observation, actionTaken, remarks, startDate,
            endDate, startTime, endTime, costCenter, labourHrs;
    long reportedDate;

    public SearchResponse() {
    }

    public SearchResponse(String frId, String clientFrId, String customerRefId, String requestorName,
                          String requestorContactNo, String location, String building, String division,
                          String locationDesc, String faultCategory, String faultCategoryName, String priority,
                          String department, String maintGrp, String status, String reportedTime,
                          String equipment, String observation, String actionTaken, String remarks, String startDate,
                          String endDate, String startTime, String endTime, String costCenter, String labourHrs, long reportedDate) {
        this.frId = frId;
        this.clientFrId = clientFrId;
        this.customerRefId = customerRefId;
        this.requestorName = requestorName;
        this.requestorContactNo = requestorContactNo;
        this.location = location;
        this.building = building;
        this.division = division;
        this.locationDesc = locationDesc;
        this.faultCategory = faultCategory;
        this.faultCategoryName = faultCategoryName;
        this.priority = priority;
        this.department = department;
        this.maintGrp = maintGrp;
        this.status = status;
        this.reportedTime = reportedTime;
        this.equipment = equipment;
        this.observation = observation;
        this.actionTaken = actionTaken;
        this.remarks = remarks;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.costCenter = costCenter;
        this.labourHrs = labourHrs;
        this.reportedDate = reportedDate;
    }

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public String getClientFrId() {
        return clientFrId;
    }

    public void setClientFrId(String clientFrId) {
        this.clientFrId = clientFrId;
    }

    public String getCustomerRefId() {
        return customerRefId;
    }

    public void setCustomerRefId(String customerRefId) {
        this.customerRefId = customerRefId;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public String getFaultCategory() {
        return faultCategory;
    }

    public void setFaultCategory(String faultCategory) {
        this.faultCategory = faultCategory;
    }

    public String getFaultCategoryName() {
        return faultCategoryName;
    }

    public void setFaultCategoryName(String faultCategoryName) {
        this.faultCategoryName = faultCategoryName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMaintGrp() {
        return maintGrp;
    }

    public void setMaintGrp(String maintGrp) {
        this.maintGrp = maintGrp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(String reportedTime) {
        this.reportedTime = reportedTime;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getLabourHrs() {
        return labourHrs;
    }

    public void setLabourHrs(String labourHrs) {
        this.labourHrs = labourHrs;
    }

    public long getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(long reportedDate) {
        this.reportedDate = reportedDate;
    }
/*
    public void setTokenGen(String tokenGen) {
        this.tokenGen = tokenGen;
    }

    public SearchResponse() {
    }

    public SearchResponse(String user, String frId, long reportedDate, long createdDate, String status,
                          String building, String location, String tokenGen, String workspaceSearch) {
        this.frId = frId;
        this.user = user;
        this.reportedDate = reportedDate;
        this.createdDate = createdDate;
        this.status = status;
        this.building = building;
        this.location = location;
        this.tokenGen = tokenGen;
        this.workspaceSearch = workspaceSearch;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;
    private long createdDate;
    private String status;
    private String building;
    private String location;
    private String frId;
    private long reportedDate;
    private String tokenGen;
    private String workspaceSearch;

    public String getWorkspaceSearch() {
        return workspaceSearch;
    }

    public void setWorkspaceSearch(String workspaceSearch) {
        this.workspaceSearch = workspaceSearch;
    }

    public String getTokenGen() {
        return tokenGen;
    }

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public long getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(long reportedDate) {
        this.reportedDate = reportedDate;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Charset toLowerCase() {
        return null;
    }*/
}