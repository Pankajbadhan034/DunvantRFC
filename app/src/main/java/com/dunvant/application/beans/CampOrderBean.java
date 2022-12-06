package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CampOrderBean implements Serializable {

    private String orderId;
    private String campId;
    private String campName;
    private String campFromDate;
    private String campToDate;
    private String locationId;
    private String locationName;
    private String orderDate;
    private String showOrderDate;
    private String netAmount;
    private String campSessionId;
    private String fromTime;
    private String toTime;
    private String showFromTime;
    private String showToTime;
    private String childName;
    private String totalBookedDays;
    private String bookingDates;
    private String showBookingDates;
    private String groupName;
    private boolean rebookFlag;
    private String fileTitle;
    private String filePath;
    private String refundAmount;
    private String displayRefundAmount;
    private String display_custom_discount;

    private String total;
    private String discount;
    private String orderAmount;

    private ArrayList<CampBookingDetailsBean> campBookingDetailsList;
    private ArrayList<BookingHistoryDiscountBean> inlineDiscountsList;
    private ArrayList<BookingHistoryRefundDetailsBean> refundDetailsList;

    private SurplusChargesBean surplusChargesBean;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getShowOrderDate() {
        return showOrderDate;
    }

    public void setShowOrderDate(String showOrderDate) {
        this.showOrderDate = showOrderDate;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getCampSessionId() {
        return campSessionId;
    }

    public void setCampSessionId(String campSessionId) {
        this.campSessionId = campSessionId;
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

    public String getShowFromTime() {
        return showFromTime;
    }

    public void setShowFromTime(String showFromTime) {
        this.showFromTime = showFromTime;
    }

    public String getShowToTime() {
        return showToTime;
    }

    public void setShowToTime(String showToTime) {
        this.showToTime = showToTime;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getTotalBookedDays() {
        return totalBookedDays;
    }

    public void setTotalBookedDays(String totalBookedDays) {
        this.totalBookedDays = totalBookedDays;
    }

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getCampFromDate() {
        return campFromDate;
    }

    public void setCampFromDate(String campFromDate) {
        this.campFromDate = campFromDate;
    }

    public String getCampToDate() {
        return campToDate;
    }

    public void setCampToDate(String campToDate) {
        this.campToDate = campToDate;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(String bookingDates) {
        this.bookingDates = bookingDates;
    }

    public String getShowBookingDates() {
        return showBookingDates;
    }

    public void setShowBookingDates(String showBookingDates) {
        this.showBookingDates = showBookingDates;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public boolean isRebookFlag() {
        return rebookFlag;
    }

    public void setRebookFlag(boolean rebookFlag) {
        this.rebookFlag = rebookFlag;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public String getDisplay_custom_discount() {
        return display_custom_discount;
    }

    public void setDisplay_custom_discount(String display_custom_discount) {
        this.display_custom_discount = display_custom_discount;
    }

    public ArrayList<CampBookingDetailsBean> getCampBookingDetailsList() {
        return campBookingDetailsList;
    }

    public void setCampBookingDetailsList(ArrayList<CampBookingDetailsBean> campBookingDetailsList) {
        this.campBookingDetailsList = campBookingDetailsList;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public SurplusChargesBean getSurplusChargesBean() {
        return surplusChargesBean;
    }

    public void setSurplusChargesBean(SurplusChargesBean surplusChargesBean) {
        this.surplusChargesBean = surplusChargesBean;
    }


}