package com.synergy.Messages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageResponse {


    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("createdDate")
    @Expose
    public long createdDate;
    @SerializedName("type")
    @Expose
    public String type;

    public MessageResponse(Integer id, String title, String text, long createdDate, String type) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.createdDate = createdDate;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public String getType() {
        return type;
    }
}

class CreatedDate {

    @SerializedName("dayOfYear")
    @Expose
    public Integer dayOfYear;
    @SerializedName("month")
    @Expose
    public String month;
    @SerializedName("year")
    @Expose
    public Integer year;
    @SerializedName("dayOfMonth")
    @Expose
    public Integer dayOfMonth;
    @SerializedName("dayOfWeek")
    @Expose
    public String dayOfWeek;
    @SerializedName("monthValue")
    @Expose
    public Integer monthValue;
    @SerializedName("hour")
    @Expose
    public Integer hour;
    @SerializedName("minute")
    @Expose
    public Integer minute;
    @SerializedName("nano")
    @Expose
    public Integer nano;
    @SerializedName("second")
    @Expose
    public Integer second;
    @SerializedName("chronology")
    @Expose
    public Chronology chronology;

    public CreatedDate(Integer dayOfYear, String month, Integer year,
                       Integer dayOfMonth, String dayOfWeek, Integer monthValue,
                       Integer hour, Integer minute, Integer nano, Integer second, Chronology chronology) {
        this.dayOfYear = dayOfYear;
        this.month = month;
        this.year = year;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.monthValue = monthValue;
        this.hour = hour;
        this.minute = minute;
        this.nano = nano;
        this.second = second;
        this.chronology = chronology;
    }

    public Integer getDayOfYear() {
        return dayOfYear;
    }

    public String getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public Integer getMonthValue() {
        return monthValue;
    }

    public Integer getHour() {
        return hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public Integer getNano() {
        return nano;
    }

    public Integer getSecond() {
        return second;
    }

    public Chronology getChronology() {
        return chronology;
    }
}

class Chronology {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("calendarType")
    @Expose
    public String calendarType;

    public Chronology(String id, String calendarType) {
        this.id = id;
        this.calendarType = calendarType;
    }

    public String getId() {
        return id;
    }

    public String getCalendarType() {
        return calendarType;
    }
}
