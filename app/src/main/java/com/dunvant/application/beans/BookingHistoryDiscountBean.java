package com.dunvant.application.beans;

import java.io.Serializable;

public class BookingHistoryDiscountBean implements Serializable{

    private String id;
    private String ordersId;
    private String discountLabel;
    private String discountDescription;
    private String discountValue;
    private String discountCode;
    private String childId;
    private String childName;
    private double discountGiven;

    private String deductType;
    private String deductValue;
    private String createdAt;

    public String getDiscountLabel() {
        return discountLabel;
    }

    public void setDiscountLabel(String discountLabel) {
        this.discountLabel = discountLabel;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }

    public void setDiscountDescription(String discountDescription) {
        this.discountDescription = discountDescription;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public double getDiscountGiven() {
        return discountGiven;
    }

    public void setDiscountGiven(double discountGiven) {
        this.discountGiven = discountGiven;
    }

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}