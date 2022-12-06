package com.dunvant.application.beans;

import java.io.Serializable;

public class ChildActivityBean implements Serializable{

    private String childId;
    private String childName;
    private String activity;
    private String addiontalData;
    private String createdAt;
    private String showCreatedAt;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAddiontalData() {
        return addiontalData;
    }

    public void setAddiontalData(String addiontalData) {
        this.addiontalData = addiontalData;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getShowCreatedAt() {
        return showCreatedAt;
    }

    public void setShowCreatedAt(String showCreatedAt) {
        this.showCreatedAt = showCreatedAt;
    }
}