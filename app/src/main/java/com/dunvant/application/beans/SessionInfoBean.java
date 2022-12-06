package com.dunvant.application.beans;

import java.io.Serializable;

public class SessionInfoBean implements Serializable{

    private String sessionId;
    private String startTimeFormatted;
    private String endTimeFormatted;
    private String sessionCost;
    private String coachingProgramsName;
    private String termsName;
    private String locationsName;
    private String groupName;
    private String fromAge;
    private String isTrial;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStartTimeFormatted() {
        return startTimeFormatted;
    }

    public void setStartTimeFormatted(String startTimeFormatted) {
        this.startTimeFormatted = startTimeFormatted;
    }

    public String getEndTimeFormatted() {
        return endTimeFormatted;
    }

    public void setEndTimeFormatted(String endTimeFormatted) {
        this.endTimeFormatted = endTimeFormatted;
    }

    public String getSessionCost() {
        return sessionCost;
    }

    public void setSessionCost(String sessionCost) {
        this.sessionCost = sessionCost;
    }

    public String getCoachingProgramsName() {
        return coachingProgramsName;
    }

    public void setCoachingProgramsName(String coachingProgramsName) {
        this.coachingProgramsName = coachingProgramsName;
    }

    public String getTermsName() {
        return termsName;
    }

    public void setTermsName(String termsName) {
        this.termsName = termsName;
    }

    public String getLocationsName() {
        return locationsName;
    }

    public void setLocationsName(String locationsName) {
        this.locationsName = locationsName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFromAge() {
        return fromAge;
    }

    public void setFromAge(String fromAge) {
        this.fromAge = fromAge;
    }

    public String getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
    }
}