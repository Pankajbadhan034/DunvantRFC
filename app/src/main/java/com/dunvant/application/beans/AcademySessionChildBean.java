package com.dunvant.application.beans;

import java.io.Serializable;

public class AcademySessionChildBean implements Serializable{

    private String sessionId;
    private String childrenIdsCSV;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getChildrenIdsCSV() {
        return childrenIdsCSV;
    }

    public void setChildrenIdsCSV(String childrenIdsCSV) {
        this.childrenIdsCSV = childrenIdsCSV;
    }
}