package com.dunvant.application.beans;

import java.io.Serializable;

public class SurplusChargesBean implements Serializable{

    private String id;
    private String ordersId;
    private String chargeLabel;
    private String chargeValue;
    private String createdAt;

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

    public String getChargeLabel() {
        return chargeLabel;
    }

    public void setChargeLabel(String chargeLabel) {
        this.chargeLabel = chargeLabel;
    }

    public String getChargeValue() {
        return chargeValue;
    }

    public void setChargeValue(String chargeValue) {
        this.chargeValue = chargeValue;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}