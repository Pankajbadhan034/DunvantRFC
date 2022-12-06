package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchExcludedDatesBean implements Serializable{

    private String excludedDate;
    private String excludedFromTime;
    private String excludedToTime;

    public String getExcludedDate() {
        return excludedDate;
    }

    public void setExcludedDate(String excludedDate) {
        this.excludedDate = excludedDate;
    }

    public String getExcludedFromTime() {
        return excludedFromTime;
    }

    public void setExcludedFromTime(String excludedFromTime) {
        this.excludedFromTime = excludedFromTime;
    }

    public String getExcludedToTime() {
        return excludedToTime;
    }

    public void setExcludedToTime(String excludedToTime) {
        this.excludedToTime = excludedToTime;
    }
}