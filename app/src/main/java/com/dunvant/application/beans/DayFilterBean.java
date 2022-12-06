package com.dunvant.application.beans;

import java.io.Serializable;

public class DayFilterBean implements Serializable {
    private String day;
    private String dayId;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }
}
