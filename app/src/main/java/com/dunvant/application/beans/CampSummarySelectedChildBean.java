package com.dunvant.application.beans;

import java.io.Serializable;

public class CampSummarySelectedChildBean implements Serializable{

    private String totalCost;
    private String perDayCost;
    private String bookingDates;
    private String weeklyDiscount;
    private String campCost;
    private String name;
    private String netPay;
    private String discountCost;
    private String childId;
    private DiscountBean discountBean;

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getPerDayCost() {
        return perDayCost;
    }

    public void setPerDayCost(String perDayCost) {
        this.perDayCost = perDayCost;
    }

    public String getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(String bookingDates) {
        this.bookingDates = bookingDates;
    }

    public String getWeeklyDiscount() {
        return weeklyDiscount;
    }

    public void setWeeklyDiscount(String weeklyDiscount) {
        this.weeklyDiscount = weeklyDiscount;
    }

    public String getCampCost() {
        return campCost;
    }

    public void setCampCost(String campCost) {
        this.campCost = campCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    public String getDiscountCost() {
        return discountCost;
    }

    public void setDiscountCost(String discountCost) {
        this.discountCost = discountCost;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public DiscountBean getDiscountBean() {
        return discountBean;
    }

    public void setDiscountBean(DiscountBean discountBean) {
        this.discountBean = discountBean;
    }
}