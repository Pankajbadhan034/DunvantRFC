package com.dunvant.application.beans;

import java.io.Serializable;

public class CoachBean implements Serializable{

    private String coachId;
    private String fullName;

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}