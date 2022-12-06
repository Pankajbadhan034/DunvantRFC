package com.dunvant.application.beans;

import java.io.Serializable;

public class AcademyLocationBean implements Serializable{

    private String locationId;
    private String locationName;
    private String description;
    private double latitude;
    private double longitude;
    private String coachingProgramIds;
    private String coachingProgramNames;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCoachingProgramIds() {
        return coachingProgramIds;
    }

    public void setCoachingProgramIds(String coachingProgramIds) {
        this.coachingProgramIds = coachingProgramIds;
    }

    public String getCoachingProgramNames() {
        return coachingProgramNames;
    }

    public void setCoachingProgramNames(String coachingProgramNames) {
        this.coachingProgramNames = coachingProgramNames;
    }
}