package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchPriceDetailBean implements Serializable{

    private String pitchesId;
    private String day;
    private String fromTime;
    private String toTime;
    private String hourPrice;
    private String pitchType;
    private String hourType;
    private String fromTimeFormatted;
    private String toTimeFormatted;

    public String getPitchesId() {
        return pitchesId;
    }

    public void setPitchesId(String pitchesId) {
        this.pitchesId = pitchesId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(String hourPrice) {
        this.hourPrice = hourPrice;
    }

    public String getPitchType() {
        return pitchType;
    }

    public void setPitchType(String pitchType) {
        this.pitchType = pitchType;
    }

    public String getHourType() {
        return hourType;
    }

    public void setHourType(String hourType) {
        this.hourType = hourType;
    }

    public String getFromTimeFormatted() {
        return fromTimeFormatted;
    }

    public void setFromTimeFormatted(String fromTimeFormatted) {
        this.fromTimeFormatted = fromTimeFormatted;
    }

    public String getToTimeFormatted() {
        return toTimeFormatted;
    }

    public void setToTimeFormatted(String toTimeFormatted) {
        this.toTimeFormatted = toTimeFormatted;
    }
}