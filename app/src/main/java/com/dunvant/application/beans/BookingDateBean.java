package com.dunvant.application.beans;

import java.io.Serializable;

public class BookingDateBean implements Serializable{

    private String id;
    private String bookingDate;
    private String bookingDateFormatted;
    private String bookingDateDay;
    private String cost;
    private String totalCost;
    private String numberOfHours;
    private String sessionCost;
    private String childName;
    private String childId;
    private String childAge;
    private String orderSessionsId;
    private String dobFormatted;
    private String campsId;
    private String weeklyDiscount;
    private String ageValue;

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingDateFormatted() {
        return bookingDateFormatted;
    }

    public void setBookingDateFormatted(String bookingDateFormatted) {
        this.bookingDateFormatted = bookingDateFormatted;
    }

    public String getBookingDateDay() {
        return bookingDateDay;
    }

    public void setBookingDateDay(String bookingDateDay) {
        this.bookingDateDay = bookingDateDay;
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

    public String getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(String numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public String getSessionCost() {
        return sessionCost;
    }

    public void setSessionCost(String sessionCost) {
        this.sessionCost = sessionCost;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public String getOrderSessionsId() {
        return orderSessionsId;
    }

    public void setOrderSessionsId(String orderSessionsId) {
        this.orderSessionsId = orderSessionsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDobFormatted() {
        return dobFormatted;
    }

    public void setDobFormatted(String dobFormatted) {
        this.dobFormatted = dobFormatted;
    }

    public String getCampsId() {
        return campsId;
    }

    public void setCampsId(String campsId) {
        this.campsId = campsId;
    }

    public String getWeeklyDiscount() {
        return weeklyDiscount;
    }

    public void setWeeklyDiscount(String weeklyDiscount) {
        this.weeklyDiscount = weeklyDiscount;
    }

    public String getAgeValue() {
        return ageValue;
    }

    public void setAgeValue(String ageValue) {
        this.ageValue = ageValue;
    }
}