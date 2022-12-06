package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class PitchSummaryDataBean implements Serializable{

    private String pitchId;
    private String pitchName;
    private String locationName;
    private String fromDate;
    private String toDate;
//    private String prices;
//    private String bulkHoursDiscount;
//    private String bulkHours;
//    private String additionalBookingDiscount;
//    private String showPrice;
    private String isFullPitch;
//    private String unavailableDates;

    private double initialAmountValue;
//    private double bulkHourDiscountValue;
//    private double additionalDiscountValue;

    private String bulkHourDiscountAmount;
    private String additionalDiscountAmount;
    private String additionalDiscountLabel;

    private ArrayList<PitchBookingDateBean> bookingDatesList = new ArrayList<>();

    public String getPitchId() {
        return pitchId;
    }

    public void setPitchId(String pitchId) {
        this.pitchId = pitchId;
    }

    public String getPitchName() {
        return pitchName;
    }

    public void setPitchName(String pitchName) {
        this.pitchName = pitchName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /*public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getBulkHoursDiscount() {
        return bulkHoursDiscount;
    }

    public void setBulkHoursDiscount(String bulkHoursDiscount) {
        this.bulkHoursDiscount = bulkHoursDiscount;
    }

    public String getBulkHours() {
        return bulkHours;
    }

    public void setBulkHours(String bulkHours) {
        this.bulkHours = bulkHours;
    }

    public String getAdditionalBookingDiscount() {
        return additionalBookingDiscount;
    }

    public void setAdditionalBookingDiscount(String additionalBookingDiscount) {
        this.additionalBookingDiscount = additionalBookingDiscount;
    }

    public String getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(String showPrice) {
        this.showPrice = showPrice;
    }*/

    /*public String getUnavailableDates() {
        return unavailableDates;
    }

    public void setUnavailableDates(String unavailableDates) {
        this.unavailableDates = unavailableDates;
    }*/

    public ArrayList<PitchBookingDateBean> getBookingDatesList() {
        return bookingDatesList;
    }

    public void setBookingDatesList(ArrayList<PitchBookingDateBean> bookingDatesList) {
        this.bookingDatesList = bookingDatesList;
    }

    public double getInitialAmountValue() {
        return initialAmountValue;
    }

    public void setInitialAmountValue(double initialAmountValue) {
        this.initialAmountValue = initialAmountValue;
    }

    /*
    public double getBulkHourDiscountValue() {
        return bulkHourDiscountValue;
    }

    public void setBulkHourDiscountValue(double bulkHourDiscountValue) {
        this.bulkHourDiscountValue = bulkHourDiscountValue;
    }

    public double getAdditionalDiscountValue() {
        return additionalDiscountValue;
    }

    public void setAdditionalDiscountValue(double additionalDiscountValue) {
        this.additionalDiscountValue = additionalDiscountValue;
    }*/

    public String getIsFullPitch() {
        return isFullPitch;
    }

    public void setIsFullPitch(String isFullPitch) {
        this.isFullPitch = isFullPitch;
    }

    public String getBulkHourDiscountAmount() {
        return bulkHourDiscountAmount;
    }

    public void setBulkHourDiscountAmount(String bulkHourDiscountAmount) {
        this.bulkHourDiscountAmount = bulkHourDiscountAmount;
    }

    public String getAdditionalDiscountAmount() {
        return additionalDiscountAmount;
    }

    public void setAdditionalDiscountAmount(String additionalDiscountAmount) {
        this.additionalDiscountAmount = additionalDiscountAmount;
    }

    public String getAdditionalDiscountLabel() {
        return additionalDiscountLabel;
    }

    public void setAdditionalDiscountLabel(String additionalDiscountLabel) {
        this.additionalDiscountLabel = additionalDiscountLabel;
    }
}