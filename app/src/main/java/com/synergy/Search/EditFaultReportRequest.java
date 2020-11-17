package com.synergy.Search;


import java.util.ArrayList;
import java.util.List;

public class EditFaultReportRequest {

    private String frId;

    private Building building;

    private Location location;

    private String requestorName;

    private Department department;

    private String requestorContactNo;

    private String locationDesc;

    private FaultCategory faultCategory;

    private String faultCategoryDesc;

    private Priority priority;

    private MaintGrp maintGrp;

    private Division division;

    private String observation;

    private String diagnosis;

    private String actionTaken;

    private CostCenter costCenter =null;

    private String status;

    private Equipment equipment;

    private List<String> remarks = null;

    private ArrayList<AttendedBy> attendedBy;

    public EditFaultReportRequest(String frId, Building building, Location location, String reqtorName,
                                  Department department, String reqtorContactNo, String locOtherDesc,
                                  FaultCategory faultCategory, String faultCategoryDesc, Priority priority,
                                  MaintGrp maintGrp, Division division, String observe, String diagnosis, String actionTaken,
                                  CostCenter costCenter, String status, Equipment equipment,
                                  List<String> remarks, ArrayList<AttendedBy> attendedBy) {
        this.frId = frId;
        this.building = building;
        this.location = location;
        this.requestorName = reqtorName;
        this.department = department;
        this.requestorContactNo = reqtorContactNo;
        this.locationDesc = locOtherDesc;
        this.faultCategory = faultCategory;
        this.faultCategoryDesc = faultCategoryDesc;
        this.priority = priority;
        this.maintGrp = maintGrp;
        this.division = division;
        this.observation = observe;
        this.diagnosis = diagnosis;
        this.actionTaken = actionTaken;
        this.costCenter = costCenter;
        this.status = status;
        this.equipment = equipment;
        this.remarks = remarks;
        this.attendedBy = attendedBy;
    }


    public void setFrId(String frId) {
        this.frId = frId;
    }

    public void setBldgId(Building bldgId) {
        this.building = bldgId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setRequestorContactNo(String requestorContactNo) {
        this.requestorContactNo = requestorContactNo;
    }


    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public void setFaultCategory(FaultCategory faultCategory) {
        this.faultCategory = faultCategory;
    }

    public void setFaultCategoryName(String faultCategoryName) {
        this.faultCategoryDesc = faultCategoryName;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setMaintGrp(MaintGrp maintGrp) {
        this.maintGrp = maintGrp;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }

    public void setAttendedBy(ArrayList<AttendedBy> attendedBy) {
        this.attendedBy = attendedBy;
    }

    public String getFrId() {
        return frId;
    }

    public Building getBldgId() {
        return building;
    }

    public Location getLocation() {
        return location;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public Department getDepartment() {
        return department;
    }

    public String getRequestorContactNo() {
        return requestorContactNo;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public FaultCategory getFaultCategory() {
        return faultCategory;
    }

    public String getFaultCategoryName() {
        return faultCategoryDesc;
    }

    public Priority getPriority() {
        return priority;
    }

    public MaintGrp getMaintGrp() {
        return maintGrp;
    }

    public Division getDivision() {
        return division;
    }

    public String getObservation() {
        return observation;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public CostCenter getCostCenter() {
        return costCenter;
    }

    public String getStatus() {
        return status;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public ArrayList<AttendedBy> getAttendedBy() {
        return attendedBy;
    }
/* public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public Integer getBldgId() {
        return bldgId;
    }

    public void setBldgId(Integer bldgId) {
        this.bldgId = bldgId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public String getReqtorName() {
        return reqtorName;
    }

    public void setReqtorName(String reqtorName) {
        this.reqtorName = reqtorName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getReqtorContactNo() {
        return reqtorContactNo;
    }

    public void setReqtorContactNo(String reqtorContactNo) {
        this.reqtorContactNo = reqtorContactNo;
    }

    public long getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(long reportedDate) {
        this.reportedDate = reportedDate;
    }

    public long getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(long reportedTime) {
        this.reportedTime = reportedTime;
    }

    public long getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(long responseDate) {
        this.responseDate = responseDate;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getLocOtherDesc() {
        return locOtherDesc;
    }

    public void setLocOtherDesc(String locOtherDesc) {
        this.locOtherDesc = locOtherDesc;
    }

    public Integer getFaultCodeId() {
        return faultCodeId;
    }

    public void setFaultCodeId(Integer faultCodeId) {
        this.faultCodeId = faultCodeId;
    }

    public String getFaultDtl() {
        return faultDtl;
    }

    public void setFaultDtl(String faultDtl) {
        this.faultDtl = faultDtl;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public Integer getMaintGrpId() {
        return maintGrpId;
    }

    public void setMaintGrpId(Integer maintGrpId) {
        this.maintGrpId = maintGrpId;
    }

    public Object getDivision() {
        return division;
    }

    public void setDivision(Integer division) {
        this.division = division;
    }

    public String getObserve() {
        return observe;
    }

    public void setObserve(String observe) {
        this.observe = observe;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public Integer getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(Integer costCenter) {
        this.costCenter = costCenter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }

    public Object getAttendedBy() {
        return attendedBy;
    }

    public void setAttendedBy(Integer attendedBy) {
        this.attendedBy = attendedBy;
    }*/

/*
    public EditFaultReportRequest(String frId, Integer bldgId, Integer locId,
                                  String reqtorName, Integer deptId, String reqtorContactNo,
                                  long reportedDate, long reportedTime, long responseDate,
                                  long responseTime, long startDate, long startTime, long endDate,
                                  long endTime, String locOtherDesc, Integer faultCodeId, String faultDtl,
                                  Integer priorityId, Integer maintGrpId, Integer division, String observe,
                                  String diagnosis, String actionTaken, Integer costCenter, String status,
                                  String equipment, List<String> remarks, Integer attendedBy) {
        this.frId = frId;
        this.bldgId = bldgId;
        this.locId = locId;
        this.reqtorName = reqtorName;
        this.deptId = deptId;
        this.reqtorContactNo = reqtorContactNo;
        this.reportedDate = reportedDate;
        this.reportedTime = reportedTime;
        this.responseDate = responseDate;
        this.responseTime = responseTime;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.locOtherDesc = locOtherDesc;
        this.faultCodeId = faultCodeId;
        this.faultDtl = faultDtl;
        this.priorityId = priorityId;
        this.maintGrpId = maintGrpId;
        this.division = division;
        this.observe = observe;
        this.diagnosis = diagnosis;
        this.actionTaken = actionTaken;
        this.costCenter = costCenter;
        this.status = status;
        this.equipment = equipment;
        this.remarks = remarks;
        this.attendedBy = attendedBy;
    }
*/
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

class CostCenter {


    private Integer id=null;
    public CostCenter(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [ id = " + id + "]";
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
        return "ClassPojo [ id = " + id + "]";
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
        return "ClassPojo [ id = " + id + "]";
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
        return "ClassPojo [ id = " + id + "]";
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
        return "ClassPojo [  id = " + id + "]";
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
        return "ClassPojo [  id = " + id + "]";
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
        return "ClassPojo [ id = " + id + "]";
    }
}

class AttendedBy {
    int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class Equipment {
    int id;

    public Equipment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}