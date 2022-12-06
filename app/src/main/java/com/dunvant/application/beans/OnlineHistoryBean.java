package com.dunvant.application.beans;

import java.io.Serializable;

public class OnlineHistoryBean implements Serializable {
    String id;
    String netAmount;
    String displayTotalCost;
    String orderDate;
    String state;
    String refundAmount;
    String customDiscount;
    String displayCustomDiscount;
    String displayRefundAmount;
    String stateCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getDisplayTotalCost() {
        return displayTotalCost;
    }

    public void setDisplayTotalCost(String displayTotalCost) {
        this.displayTotalCost = displayTotalCost;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCustomDiscount() {
        return customDiscount;
    }

    public void setCustomDiscount(String customDiscount) {
        this.customDiscount = customDiscount;
    }

    public String getDisplayCustomDiscount() {
        return displayCustomDiscount;
    }

    public void setDisplayCustomDiscount(String displayCustomDiscount) {
        this.displayCustomDiscount = displayCustomDiscount;
    }

    public String getDisplayRefundAmount() {
        return displayRefundAmount;
    }

    public void setDisplayRefundAmount(String displayRefundAmount) {
        this.displayRefundAmount = displayRefundAmount;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
