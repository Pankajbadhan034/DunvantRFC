package com.dunvant.application.beans;

import java.io.Serializable;

public class CoachingProgramBean implements Serializable{

    private String coachingProgramId;
    private String coachinProgramName;

    public String getCoachingProgramId() {
        return coachingProgramId;
    }

    public void setCoachingProgramId(String coachingProgramId) {
        this.coachingProgramId = coachingProgramId;
    }

    public String getCoachinProgramName() {
        return coachinProgramName;
    }

    public void setCoachinProgramName(String coachinProgramName) {
        this.coachinProgramName = coachinProgramName;
    }
}