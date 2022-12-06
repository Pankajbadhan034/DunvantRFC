package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchTypeBean implements Serializable{

    /*private PitchTimeTypeBean offRate;
    private PitchTimeTypeBean normalRate;
    private PitchTimeTypeBean peakRate;*/

    private String pitchesId;
    private String fromDate;
    private String toDate;
    private String day;
    private String fromTime;
    private String toTime;
    private String hourPrice;
    private String pitchType;
    private String fromTimeFormatted;
    private String toTimeFormatted;
    private String fromDateFormatted;
    private String toDateFormatted;
    private String dayLabel;

    public String getPitchesId() {
        return pitchesId;
    }

    public void setPitchesId(String pitchesId) {
        this.pitchesId = pitchesId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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

    public String getFromDateFormatted() {
        return fromDateFormatted;
    }

    public void setFromDateFormatted(String fromDateFormatted) {
        this.fromDateFormatted = fromDateFormatted;
    }

    public String getToDateFormatted() {
        return toDateFormatted;
    }

    public void setToDateFormatted(String toDateFormatted) {
        this.toDateFormatted = toDateFormatted;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    /*public PitchTimeTypeBean getOffRate() {
        return offRate;
    }

    public void setOffRate(PitchTimeTypeBean offRate) {
        this.offRate = offRate;
    }

    public PitchTimeTypeBean getNormalRate() {
        return normalRate;
    }

    public void setNormalRate(PitchTimeTypeBean normalRate) {
        this.normalRate = normalRate;
    }

    public PitchTimeTypeBean getPeakRate() {
        return peakRate;
    }

    public void setPeakRate(PitchTimeTypeBean peakRate) {
        this.peakRate = peakRate;
    }*/
}