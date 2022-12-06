package com.dunvant.application.beans;

import java.io.Serializable;

public class AgeGroupFilterBean implements Serializable {
    private String ageGroupId;
    private String academiesId;
    private String groupName;
    private String fromAge;
    private String toAge;
    private String ageGroupType;
    private String createdAt;
    private String state;

    public String getAgeGroupId() {
        return ageGroupId;
    }

    public void setAgeGroupId(String ageGroupId) {
        this.ageGroupId = ageGroupId;
    }

    public String getAcademiesId() {
        return academiesId;
    }

    public void setAcademiesId(String academiesId) {
        this.academiesId = academiesId;
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

    public String getToAge() {
        return toAge;
    }

    public void setToAge(String toAge) {
        this.toAge = toAge;
    }

    public String getAgeGroupType() {
        return ageGroupType;
    }

    public void setAgeGroupType(String ageGroupType) {
        this.ageGroupType = ageGroupType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
