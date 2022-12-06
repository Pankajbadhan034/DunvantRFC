package com.dunvant.application.beans;

import java.io.Serializable;

public class CalendarListBean implements Serializable {
    String oragniserName;
    String dateTimeFormatted;
    String dateTimeMilli;
    String eventDetail;
    String startTime;
    String endTime;


    public String getOragniserName() {
        return oragniserName;
    }

    public void setOragniserName(String oragniserName) {
        this.oragniserName = oragniserName;
    }

    public String getDateTimeFormatted() {
        return dateTimeFormatted;
    }

    public void setDateTimeFormatted(String dateTimeFormatted) {
        this.dateTimeFormatted = dateTimeFormatted;
    }

    public String getDateTimeMilli() {
        return dateTimeMilli;
    }

    public void setDateTimeMilli(String dateTimeMilli) {
        this.dateTimeMilli = dateTimeMilli;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
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
}
