package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchTimeTypeBean implements Serializable{

    private String timeRange;
    private PitchPriceDetailBean friday;
    private PitchPriceDetailBean saturday;
    private PitchPriceDetailBean SundayToThursday;

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public PitchPriceDetailBean getFriday() {
        return friday;
    }

    public void setFriday(PitchPriceDetailBean friday) {
        this.friday = friday;
    }

    public PitchPriceDetailBean getSaturday() {
        return saturday;
    }

    public void setSaturday(PitchPriceDetailBean saturday) {
        this.saturday = saturday;
    }

    public PitchPriceDetailBean getSundayToThursday() {
        return SundayToThursday;
    }

    public void setSundayToThursday(PitchPriceDetailBean sundayToThursday) {
        SundayToThursday = sundayToThursday;
    }
}