package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchAvailabilityBean implements Serializable{

    private String day;
    private String availabilityFromTime;
    private String availabilityToTime;
    private String availabilityIsTimeOff;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getAvailabilityFromTime() {
        return availabilityFromTime;
    }

    public void setAvailabilityFromTime(String availabilityFromTime) {
        this.availabilityFromTime = availabilityFromTime;
    }

    public String getAvailabilityToTime() {
        return availabilityToTime;
    }

    public void setAvailabilityToTime(String availabilityToTime) {
        this.availabilityToTime = availabilityToTime;
    }

    public String getAvailabilityIsTimeOff() {
        return availabilityIsTimeOff;
    }

    public void setAvailabilityIsTimeOff(String availabilityIsTimeOff) {
        this.availabilityIsTimeOff = availabilityIsTimeOff;
    }
}