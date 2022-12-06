package com.dunvant.application.beans;

import java.io.Serializable;

public class ActivityStatBean implements Serializable{

    private String coachingProgramName;
    private String coachingProgramImage;
    private String sessionDay;
    private String startTime;
    private String endTime;
    private String locationName;
    private String totalSessions;
    private String remainingSessions;
    private String attendedSessions;

    private ChildBean childBean;
    private ActivityStatComponentBean distanceBean;
    private ActivityStatComponentBean stepsBean;
    private ActivityStatComponentBean caloriesBean;
    private ActivityStatComponentBean heartRateBean;

    public String getCoachingProgramName() {
        return coachingProgramName;
    }

    public void setCoachingProgramName(String coachingProgramName) {
        this.coachingProgramName = coachingProgramName;
    }

    public String getCoachingProgramImage() {
        return coachingProgramImage;
    }

    public void setCoachingProgramImage(String coachingProgramImage) {
        this.coachingProgramImage = coachingProgramImage;
    }

    public String getSessionDay() {
        return sessionDay;
    }

    public void setSessionDay(String sessionDay) {
        this.sessionDay = sessionDay;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(String totalSessions) {
        this.totalSessions = totalSessions;
    }

    public String getRemainingSessions() {
        return remainingSessions;
    }

    public void setRemainingSessions(String remainingSessions) {
        this.remainingSessions = remainingSessions;
    }

    public String getAttendedSessions() {
        return attendedSessions;
    }

    public void setAttendedSessions(String attendedSessions) {
        this.attendedSessions = attendedSessions;
    }

    public ChildBean getChildBean() {
        return childBean;
    }

    public void setChildBean(ChildBean childBean) {
        this.childBean = childBean;
    }

    public ActivityStatComponentBean getDistanceBean() {
        return distanceBean;
    }

    public void setDistanceBean(ActivityStatComponentBean distanceBean) {
        this.distanceBean = distanceBean;
    }

    public ActivityStatComponentBean getStepsBean() {
        return stepsBean;
    }

    public void setStepsBean(ActivityStatComponentBean stepsBean) {
        this.stepsBean = stepsBean;
    }

    public ActivityStatComponentBean getCaloriesBean() {
        return caloriesBean;
    }

    public void setCaloriesBean(ActivityStatComponentBean caloriesBean) {
        this.caloriesBean = caloriesBean;
    }

    public ActivityStatComponentBean getHeartRateBean() {
        return heartRateBean;
    }

    public void setHeartRateBean(ActivityStatComponentBean heartRateBean) {
        this.heartRateBean = heartRateBean;
    }
}