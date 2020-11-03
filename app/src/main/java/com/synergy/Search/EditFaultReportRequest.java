package com.synergy.Search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;

public class EditFaultReportRequest {
    @SerializedName("frId")
    @Expose
    private String frId;
    @SerializedName("building")
    @Expose
    private Building building;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("requestorName")
    @Expose
    private String requestorName;
    @SerializedName("department")
    @Expose
    private Department department;
    @SerializedName("reqtorContactNo")
    @Expose
    private String reqtorContactNo;
    @SerializedName("reportedDate")
    @Expose
    private long reportedDate;
    @SerializedName("reportedTime")
    @Expose
    private long reportedTime;
    @SerializedName("responseDate")
    @Expose
    private long responseDate;
    @SerializedName("responseTime")
    @Expose
    private long responseTime;
    @SerializedName("startDate")
    @Expose
    private long startDate;
    @SerializedName("startTime")
    @Expose
    private long startTime;
    @SerializedName("endDate")
    @Expose
    private long endDate;
    @SerializedName("endTime")
    @Expose
    private long endTime;
    @SerializedName("locationDesc")
    @Expose
    private String locationDesc;
    @SerializedName("faultCategory")
    @Expose
    private FaultCategory faultCategory;
    @SerializedName("faultCategoryName")
    @Expose
    private String faultCategoryName;
    @SerializedName("priority")
    @Expose
    private Priority priority;
    @SerializedName("maintGrp")
    @Expose
    private MaintGrp maintGrp;
    @SerializedName("division")
    @Expose
    private Division division;
    @SerializedName("observation")
    @Expose
    private String observation;
    @SerializedName("diagnosis")
    @Expose
    private String diagnosis;
    @SerializedName("actionTaken")
    @Expose
    private String actionTaken;
    @SerializedName("costCenter")
    @Expose
    private CostCenter costCenter;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("equipment")
    @Expose
    private Equipment equipment;
    @SerializedName("remarks")
    @Expose
    private List<String> remarks = null;
    @SerializedName("attendedBy")
    @Expose
    private ArrayList<AttendedBy> attendedBy;

    public EditFaultReportRequest(String frId, Building building, Location location, String reqtorName,
                                  Department department, String reqtorContactNo, long reportedDate,
                                  long reportedTime, long responseDate, long responseTime, long startDate,
                                  long startTime, long endDate, long endTime, String locOtherDesc,
                                  FaultCategory faultCategory, String faultDtl, Priority priority,
                                  MaintGrp maintGrp, Division division, String observe, String diagnosis, String actionTaken,
                                  CostCenter costCenter, String status, Equipment equipment,
                                  List<String> remarks, ArrayList<AttendedBy> attendedBy) {
        this.frId = frId;
        this.building = building;
        this.location = location;
        this.requestorName = reqtorName;
        this.department = department;
        this.reqtorContactNo = reqtorContactNo;
        this.reportedDate = reportedDate;
        this.reportedTime = reportedTime;
        this.responseDate = responseDate;
        this.responseTime = responseTime;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.locationDesc = locOtherDesc;
        this.faultCategory = faultCategory;
        this.faultCategoryName = faultDtl;
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

    public void setReqtorContactNo(String reqtorContactNo) {
        this.reqtorContactNo = reqtorContactNo;
    }

    public void setReportedDate(long reportedDate) {
        this.reportedDate = reportedDate;
    }

    public void setReportedTime(long reportedTime) {
        this.reportedTime = reportedTime;
    }

    public void setResponseDate(long responseDate) {
        this.responseDate = responseDate;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public void setFaultCategory(FaultCategory faultCategory) {
        this.faultCategory = faultCategory;
    }

    public void setFaultCategoryName(String faultCategoryName) {
        this.faultCategoryName = faultCategoryName;
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

    public String getReqtorContactNo() {
        return reqtorContactNo;
    }

    public long getReportedDate() {
        return reportedDate;
    }

    public long getReportedTime() {
        return reportedTime;
    }

    public long getResponseDate() {
        return responseDate;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public FaultCategory getFaultCategory() {
        return faultCategory;
    }

    public String getFaultCategoryName() {
        return faultCategoryName;
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

 class FaultCategory
{
    private int id;



    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+"]";
    }
}
class CostCenter{

    public CostCenter(int id) {
        this.id = id;
    }
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [ id = "+id+"]";
    }
}
 class Location
{
    private int id;
    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ id = "+id+"]";
    }
}



class Building
{
    private int id;

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ id = "+id+"]";
    }
}


class Division
{
    private int id;
    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ id = "+id+"]";
    }
}



class Priority
{
    private int id;


    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [  id = "+id+"]";
    }
}


class Department
{


    private int id;
    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [  id = "+id+"]";
    }
}


class MaintGrp
{
    private int id;



    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ id = "+id+"]";
    }
}
class  AttendedBy{
   int  id;

    public AttendedBy(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
class Equipment{
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