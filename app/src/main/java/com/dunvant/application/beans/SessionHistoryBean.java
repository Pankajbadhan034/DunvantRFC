package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class SessionHistoryBean implements Serializable{

    private String orderId;
    private String previousOrderId;
    private String sessionName;
    private String showTotalCost;
    private String orderDate;
    private String state;

    private String academiesId;
    private String orderParentId;
    private String paymentId;
    private String userId;
    private String userAddressId;
    private String totalAmount;
    private String taxAmount;
    private String discountAmount;
    private String netAmount;
    private String notes;
    private String createdAt;
    private String registrationFee;
    private String tournamentFee;
    private String discountPercentage;
    private String originalAmount;

    private ArrayList<FeesBean> feesList = new ArrayList<>();
    private ArrayList<OrderSessionBean> orderSessionsList = new ArrayList<>();
    private ArrayList<OrderSessionBeanNew> orderSessionsListNew = new ArrayList<>();

    private String refundAmount;
    private String displayRefundAmount;
    private String displayCustomDiscount;

    private ArrayList<BookingHistoryDiscountBean> inlineDiscountsList = new ArrayList<>();
    private ArrayList<BookingHistoryRefundDetailsBean> refundDetailsList = new ArrayList<>();

    private String totalIncludingFees;
    private String orderAmount;

    private boolean childMoved;
    private String latestOrderId;
    private String latestTotal;

    private SurplusChargesBean surplusChargesBean;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getShowTotalCost() {
        return showTotalCost;
    }

    public void setShowTotalCost(String showTotalCost) {
        this.showTotalCost = showTotalCost;
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

    public String getAcademiesId() {
        return academiesId;
    }

    public void setAcademiesId(String academiesId) {
        this.academiesId = academiesId;
    }

    public String getOrderParentId() {
        return orderParentId;
    }

    public void setOrderParentId(String orderParentId) {
        this.orderParentId = orderParentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(String userAddressId) {
        this.userAddressId = userAddressId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(String registrationFee) {
        this.registrationFee = registrationFee;
    }

    public String getTournamentFee() {
        return tournamentFee;
    }

    public void setTournamentFee(String tournamentFee) {
        this.tournamentFee = tournamentFee;
    }

    public ArrayList<OrderSessionBean> getOrderSessionsList() {
        return orderSessionsList;
    }

    public void setOrderSessionsList(ArrayList<OrderSessionBean> orderSessionsList) {
        this.orderSessionsList = orderSessionsList;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public ArrayList<FeesBean> getFeesList() {
        return feesList;
    }

    public void setFeesList(ArrayList<FeesBean> feesList) {
        this.feesList = feesList;
    }

    public ArrayList<OrderSessionBeanNew> getOrderSessionsListNew() {
        return orderSessionsListNew;
    }

    public void setOrderSessionsListNew(ArrayList<OrderSessionBeanNew> orderSessionsListNew) {
        this.orderSessionsListNew = orderSessionsListNew;
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

    public String getTotalIncludingFees() {
        return totalIncludingFees;
    }

    public void setTotalIncludingFees(String totalIncludingFees) {
        this.totalIncludingFees = totalIncludingFees;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPreviousOrderId() {
        return previousOrderId;
    }

    public void setPreviousOrderId(String previousOrderId) {
        this.previousOrderId = previousOrderId;
    }

    public String getLatestOrderId() {
        return latestOrderId;
    }

    public void setLatestOrderId(String latestOrderId) {
        this.latestOrderId = latestOrderId;
    }

    public String getLatestTotal() {
        return latestTotal;
    }

    public void setLatestTotal(String latestTotal) {
        this.latestTotal = latestTotal;
    }

    public boolean isChildMoved() {
        return childMoved;
    }

    public void setChildMoved(boolean childMoved) {
        this.childMoved = childMoved;
    }

    public SurplusChargesBean getSurplusChargesBean() {
        return surplusChargesBean;
    }

    public void setSurplusChargesBean(SurplusChargesBean surplusChargesBean) {
        this.surplusChargesBean = surplusChargesBean;
    }

    public String getDisplayCustomDiscount() {
        return displayCustomDiscount;
    }

    public void setDisplayCustomDiscount(String displayCustomDiscount) {
        this.displayCustomDiscount = displayCustomDiscount;
    }
}