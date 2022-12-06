package com.dunvant.application.beans;


import java.io.Serializable;

public class CampSessionBean implements Serializable{

    private String sessionId;
    private String fromTime;
    private String toTime;
    private String perDayCost;
    private String perWeekCost;
    private String groupName;
    private String showFromTime;
    private String showToTime;
    private boolean availability;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getPerDayCost() {
        return perDayCost;
    }

    public void setPerDayCost(String perDayCost) {
        this.perDayCost = perDayCost;
    }

    public String getPerWeekCost() {
        return perWeekCost;
    }

    public void setPerWeekCost(String perWeekCost) {
        this.perWeekCost = perWeekCost;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getShowFromTime() {
        return showFromTime;
    }

    public void setShowFromTime(String showFromTime) {
        this.showFromTime = showFromTime;
    }

    public String getShowToTime() {
        return showToTime;
    }

    public void setShowToTime(String showToTime) {
        this.showToTime = showToTime;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}