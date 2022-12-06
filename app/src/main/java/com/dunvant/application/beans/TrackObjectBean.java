package com.dunvant.application.beans;

import java.io.Serializable;

public class TrackObjectBean implements Serializable{

    private String id;
    private String trackWorkoutId;
    private String latitude;
    private String longitude;
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackWorkoutId() {
        return trackWorkoutId;
    }

    public void setTrackWorkoutId(String trackWorkoutId) {
        this.trackWorkoutId = trackWorkoutId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}