package com.dunvant.application.beans;

import java.io.Serializable;

public class BookingHistoryRefundDetailsBean implements Serializable{

    private String id;
    private String ordersId;
    private String amount;
    private String deductType;
    private String deductValue;
    private String netAmount;
    private String createdAt;
    private String cancelledSession;
    private String refundFee;
    private String initialAmount;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeductType() {
        return deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    public String getDeductValue() {
        return deductValue;
    }

    public void setDeductValue(String deductValue) {
        this.deductValue = deductValue;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCancelledSession() {
        return cancelledSession;
    }

    public void setCancelledSession(String cancelledSession) {
        this.cancelledSession = cancelledSession;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(String initialAmount) {
        this.initialAmount = initialAmount;
    }
}