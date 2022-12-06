package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchBookingBean implements Serializable{

    private String id;
    private String ordersId;
    private String pitchesId;
    private String bookingDate;
    private String fromTime;
    private String toTime;
    private String cost;
    private String totalCost;
    private String createdAt;
    private String showBookingDate;
    private String time;
    private String interval;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getPitchesId() {
        return pitchesId;
    }

    public void setPitchesId(String pitchesId) {
        this.pitchesId = pitchesId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getShowBookingDate() {
        return showBookingDate;
    }

    public void setShowBookingDate(String showBookingDate) {
        this.showBookingDate = showBookingDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}