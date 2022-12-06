package com.dunvant.application.beans;

import java.io.Serializable;

public class CampDaysBean implements Serializable{

    private String campId;
    private String day;
    private String dayLabel;

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }
}