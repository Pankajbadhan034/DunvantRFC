package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class AcademySessionSummaryBean implements Serializable{

    private String sessionDetailId;
    private String sessionDetailTitle;
    private String sessionId;
    private String coachingProgramName;
    private String termName;
    private String locationName;
    private String groupName;
    private String fromAge;
    private String toAge;
    private String day;
    private String fromDate;
    private String toDate;
    private String startTime;
    private String endTime;
    private String showStartTime;
    private String showEndTime;
    private String numberOfHours;
    private String cost;
    private String maxLimit;
    private String isSelectiveAllowed;
    private String isTrial;
    private int trialCount;
    private String dayLabel;
    private boolean trialSelected;
    private ArrayList<AcademySessionDateBean> availableDatesList = new ArrayList<>();
    private ArrayList<ChildBean> childrenList = new ArrayList<>();
    private ArrayList<BookingHistoryDiscountBean> discountList = new ArrayList<>();
    private double sessionCost;
    private double totalDiscount;
    private double amountAfterDiscount;
    // add by me pankaj
    private String promoCodeId;
    private String promocode;
    private String promoUsage;
    private String promoUsageCount;
    private String sids;

    public String getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(String promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public String getPromoUsage() {
        return promoUsage;
    }

    public void setPromoUsage(String promoUsage) {
        this.promoUsage = promoUsage;
    }

    public String getPromoUsageCount() {
        return promoUsageCount;
    }

    public void setPromoUsageCount(String promoUsageCount) {
        this.promoUsageCount = promoUsageCount;
    }

    public String getSids() {
        return sids;
    }

    public void setSids(String sids) {
        this.sids = sids;
    }

    public String getSessionDetailId() {
        return sessionDetailId;
    }

    public void setSessionDetailId(String sessionDetailId) {
        this.sessionDetailId = sessionDetailId;
    }

    public String getSessionDetailTitle() {
        return sessionDetailTitle;
    }

    public void setSessionDetailTitle(String sessionDetailTitle) {
        this.sessionDetailTitle = sessionDetailTitle;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCoachingProgramName() {
        return coachingProgramName;
    }

    public void setCoachingProgramName(String coachingProgramName) {
        this.coachingProgramName = coachingProgramName;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFromAge() {
        return fromAge;
    }

    public void setFromAge(String fromAge) {
        this.fromAge = fromAge;
    }

    public String getToAge() {
        return toAge;
    }

    public void setToAge(String toAge) {
        this.toAge = toAge;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(String showStartTime) {
        this.showStartTime = showStartTime;
    }

    public String getShowEndTime() {
        return showEndTime;
    }

    public void setShowEndTime(String showEndTime) {
        this.showEndTime = showEndTime;
    }

    public String getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(String numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getIsSelectiveAllowed() {
        return isSelectiveAllowed;
    }

    public void setIsSelectiveAllowed(String isSelectiveAllowed) {
        this.isSelectiveAllowed = isSelectiveAllowed;
    }

    public String getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
    }

    public int getTrialCount() {
        return trialCount;
    }

    public void setTrialCount(int trialCount) {
        this.trialCount = trialCount;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public ArrayList<AcademySessionDateBean> getAvailableDatesList() {
        return availableDatesList;
    }

    public void setAvailableDatesList(ArrayList<AcademySessionDateBean> availableDatesList) {
        this.availableDatesList = availableDatesList;
    }

    public ArrayList<ChildBean> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(ArrayList<ChildBean> childrenList) {
        this.childrenList = childrenList;
    }

    public double getSessionCost() {
        return sessionCost;
    }

    public void setSessionCost(double sessionCost) {
        this.sessionCost = sessionCost;
    }

    public boolean isTrialSelected() {
        return trialSelected;
    }

    public void setTrialSelected(boolean trialSelected) {
        this.trialSelected = trialSelected;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getAmountAfterDiscount() {
        return amountAfterDiscount;
    }

    public void setAmountAfterDiscount(double amountAfterDiscount) {
        this.amountAfterDiscount = amountAfterDiscount;
    }

    public ArrayList<BookingHistoryDiscountBean> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(ArrayList<BookingHistoryDiscountBean> discountList) {
        this.discountList = discountList;
    }
}