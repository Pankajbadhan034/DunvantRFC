package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class AgeGroupBean implements Serializable{

    private String ageGroupId;
    private String groupName;
    private ArrayList<DatesResultBean> datesResultList = new ArrayList<>();

    private String sessionId;

    public String getAgeGroupId() {
        return ageGroupId;
    }

    public void setAgeGroupId(String ageGroupId) {
        this.ageGroupId = ageGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<DatesResultBean> getDatesResultList() {
        return datesResultList;
    }

    public void setDatesResultList(ArrayList<DatesResultBean> datesResultList) {
        this.datesResultList = datesResultList;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}