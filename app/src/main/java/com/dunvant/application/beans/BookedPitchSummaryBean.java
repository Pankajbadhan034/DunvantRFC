package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class BookedPitchSummaryBean implements Serializable{

    private String id;
    private String totalCost;
    private String discountCost;
    private String netAmount;
    private String orderDate;
    private String state;
    private String refundAmount;
    private String orderAmount;

    private ArrayList<PitchHistoryDetailBean> pitchHistoryDetailListing;
    private ArrayList<BookingHistoryDiscountBean> inlineDiscountsList;
    private ArrayList<BookingHistoryRefundDetailsBean> refundDetailsList;

    private SurplusChargesBean surplusChargesBean;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getDiscountCost() {
        return discountCost;
    }

    public void setDiscountCost(String discountCost) {
        this.discountCost = discountCost;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
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

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public ArrayList<PitchHistoryDetailBean> getPitchHistoryDetailListing() {
        return pitchHistoryDetailListing;
    }

    public void setPitchHistoryDetailListing(ArrayList<PitchHistoryDetailBean> pitchHistoryDetailListing) {
        this.pitchHistoryDetailListing = pitchHistoryDetailListing;
    }

    public ArrayList<BookingHistoryDiscountBean> getInlineDiscountsList() {
        return inlineDiscountsList;
    }

    public void setInlineDiscountsList(ArrayList<BookingHistoryDiscountBean> inlineDiscountsList) {
        this.inlineDiscountsList = inlineDiscountsList;
    }

    public ArrayList<BookingHistoryRefundDetailsBean> getRefundDetailsList() {
        return refundDetailsList;
    }

    public void setRefundDetailsList(ArrayList<BookingHistoryRefundDetailsBean> refundDetailsList) {
        this.refundDetailsList = refundDetailsList;
    }

    public SurplusChargesBean getSurplusChargesBean() {
        return surplusChargesBean;
    }

    public void setSurplusChargesBean(SurplusChargesBean surplusChargesBean) {
        this.surplusChargesBean = surplusChargesBean;
    }
}