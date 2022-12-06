package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class PitchHistoryBean implements Serializable {

    private String id;
    private ArrayList<PitchItemBean> pitchesList = new ArrayList<>();
    private String displayTotalCost;
    private String orderDate;
    private String state;
    private boolean showCancellation;
    private String refundAmount;
    private String displayRefundAmount;
    private String displayCustomDiscount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<PitchItemBean> getPitchesList() {
        return pitchesList;
    }

    public void setPitchesList(ArrayList<PitchItemBean> pitchesList) {
        this.pitchesList = pitchesList;
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

    public boolean isShowCancellation() {
        return showCancellation;
    }

    public void setShowCancellation(boolean showCancellation) {
        this.showCancellation = showCancellation;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getDisplayRefundAmount() {
        return displayRefundAmount;
    }

    public void setDisplayRefundAmount(String displayRefundAmount) {
        this.displayRefundAmount = displayRefundAmount;
    }

    public String getDisplayCustomDiscount() {
        return displayCustomDiscount;
    }

    public void setDisplayCustomDiscount(String displayCustomDiscount) {
        this.displayCustomDiscount = displayCustomDiscount;
    }
}