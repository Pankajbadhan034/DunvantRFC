package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CampSummaryBean implements Serializable{

    private String totalCost;
    private String totalDiscount;
    private String netPayable;
    private String roundedNetPayable;
    private String campPerDayCost;
    private String numOfDays;
    private String totalWeeklyDiscount;
    private String promoCodeDiscount;
    private ArrayList<CampSummarySelectedChildBean> campSummarySelectedChildList;

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(String netPayable) {
        this.netPayable = netPayable;
    }

    public String getCampPerDayCost() {
        return campPerDayCost;
    }

    public void setCampPerDayCost(String campPerDayCost) {
        this.campPerDayCost = campPerDayCost;
    }

    public String getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(String numOfDays) {
        this.numOfDays = numOfDays;
    }

    public String getTotalWeeklyDiscount() {
        return totalWeeklyDiscount;
    }

    public void setTotalWeeklyDiscount(String totalWeeklyDiscount) {
        this.totalWeeklyDiscount = totalWeeklyDiscount;
    }

    public ArrayList<CampSummarySelectedChildBean> getCampSummarySelectedChildList() {
        return campSummarySelectedChildList;
    }

    public void setCampSummarySelectedChildList(ArrayList<CampSummarySelectedChildBean> campSummarySelectedChildList) {
        this.campSummarySelectedChildList = campSummarySelectedChildList;
    }

    public String getPromoCodeDiscount() {
        return promoCodeDiscount;
    }

    public void setPromoCodeDiscount(String promoCodeDiscount) {
        this.promoCodeDiscount = promoCodeDiscount;
    }

    public String getRoundedNetPayable() {
        return roundedNetPayable;
    }

    public void setRoundedNetPayable(String roundedNetPayable) {
        this.roundedNetPayable = roundedNetPayable;
    }
}