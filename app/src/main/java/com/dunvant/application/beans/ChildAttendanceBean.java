package com.dunvant.application.beans;

import java.io.Serializable;

public class ChildAttendanceBean implements Serializable{

    private String sessionId;
    private String userId;
    private String childName;
    private String bookedSessionDate;
    private String status;
    private String isTrial;
    private String unpaid;

    public String getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(String unpaid) {
        this.unpaid = unpaid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getBookedSessionDate() {
        return bookedSessionDate;
    }

    public void setBookedSessionDate(String bookedSessionDate) {
        this.bookedSessionDate = bookedSessionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
    }
}