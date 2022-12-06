package com.dunvant.application.beans;

import java.io.Serializable;

public class TrackingHistoryBean implements Serializable{

    private String id;
    private String usersId;
    private String duration;
    private String distance;
    private String speed;
    private String calories;
    private String activity;
    private String state;
    private String createdAt;
    private String showCreatedAt;
    private String showDuration;
    private String showDistance;
    private String showCalories;
    private String isShared;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getShowDuration() {
        return showDuration;
    }

    public void setShowDuration(String showDuration) {
        this.showDuration = showDuration;
    }

    public String getShowDistance() {
        return showDistance;
    }

    public void setShowDistance(String showDistance) {
        this.showDistance = showDistance;
    }

    public String getShowCalories() {
        return showCalories;
    }

    public void setShowCalories(String showCalories) {
        this.showCalories = showCalories;
    }

    public String getIsShared() {
        return isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }
}