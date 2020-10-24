package com.synergy.EquipmentSearch;

public class PmTaskResponse {
    String status;
    Schedule schedule;

    public PmTaskResponse(String status, Schedule schedule) {
        this.status = status;
        this.schedule = schedule;
    }

    public String getStatus() {
        return status;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    static class Schedule {
        long scheduleDate;
        String remarks;
        String scheduleNumber;
        String briefDescription;
        Equipment equipment;

        public Schedule(long scheduleDate, String remarks, String scheduleNumber, String briefDescription, Equipment equipment) {
            this.scheduleDate = scheduleDate;
            this.remarks = remarks;
            this.scheduleNumber = scheduleNumber;
            this.briefDescription = briefDescription;
            this.equipment = equipment;
        }

        public long getScheduleDate() {
            return scheduleDate;
        }

        public String getRemarks() {
            return remarks;
        }

        public String getScheduleNumber() {
            return scheduleNumber;
        }

        public String getBriefDescription() {
            return briefDescription;
        }

        public Equipment getEquipment() {
            return equipment;
        }

        static class Equipment {
            String equipmentCode;
            Building building;
            Location location;

            public Equipment(String equipmentCode, Building building, Location location) {
                this.equipmentCode = equipmentCode;
                this.building = building;
                this.location = location;
            }

            public String getEquipmentCode() {
                return equipmentCode;
            }

            public Building getBuilding() {
                return building;
            }

            public Location getLocation() {
                return location;
            }

            static class Building {
                String name;
                int id;

                public String getName() {
                    return name;
                }

                public int getId() {
                    return id;
                }

                public Building(String name, int id) {
                    this.name = name;
                    this.id = id;
                }
            }
            static class Location {
                String name;
                int id;

                public Location(String name, int id) {
                    this.name = name;
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public int getId() {
                    return id;
                }
            }
        }
    }
}



