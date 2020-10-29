package com.synergy.FaultReport;

public class CreateFaultRequestPojo {

    String requestorName;
    String reportedTime, requestorContactN, faultCategoryName, locationDesc;
    long reportedDate;
    Location location;
    Building building;
    MaintGrp maintGrp;
    Division division;
    Department department;
    Priority priority;
    FaultCategory faultCategory;

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(String reportedTime) {
        this.reportedTime = reportedTime;
    }

    public String getRequestorContactN() {
        return requestorContactN;
    }

    public void setRequestorContactN(String requestorContactN) {
        this.requestorContactN = requestorContactN;
    }

    public String getFaultCategoryName() {
        return faultCategoryName;
    }

    public void setFaultCategoryName(String faultCategoryName) {
        this.faultCategoryName = faultCategoryName;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public long getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(long reportedDate) {
        this.reportedDate = reportedDate;
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
    //: { "Obj1" : { "key1" : "val1" } }


    public CreateFaultRequestPojo(String requestorName, String reportedTime, Building building, Location location,
                                  String requestorContactN, long reportedDate,
                                  Priority priority, MaintGrp maintGrp, FaultCategory faultCategory, Department department,
                                  String faultCategoryDesc, String locationDesc,Division division) {

        this.division=division;
        this.faultCategoryName = faultCategoryDesc;
        this.locationDesc = locationDesc;
        this.requestorName = requestorName;
        this.reportedTime = reportedTime;
        this.building = building;
        this.location = location;
        this.requestorContactN = requestorContactN;
        this.reportedDate = reportedDate;
        this.priority = priority;
        this.maintGrp = maintGrp;
        this.faultCategory = faultCategory;
        this.department = department;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [ faultCategory = "+faultCategory+", maintGrp = "+maintGrp+", priority = "+priority+"," +
                " building = "+building+", faultCategoryName = "+faultCategoryName+", division = "+division+", " +
                "reportedDate = "+reportedDate+", locationDesc = "+locationDesc+", location = "+location+"," +
                " requestorName = "+requestorName+", department = "+department+", reportedTime = "+reportedTime+"]";
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
        return "ClassPojo [id = "+id+"]";
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
        return "ClassPojo [id = "+id+"]";
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
        return "ClassPojo [id = "+id+"]";
    }
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
        return "ClassPojo [id = "+id+"]";
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
        return "ClassPojo [id = "+id+"]";
    }
}
 class  Location{
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

