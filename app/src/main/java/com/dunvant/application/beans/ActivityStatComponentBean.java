package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ActivityStatComponentBean implements Serializable{

    private String total;
    private String average;
    private String start;
    private String end;
    private String name;
    private String colorCode;
    private String percentage;

    private ArrayList<ActivityStatComponentDetailBean> detailBeansList;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public ArrayList<ActivityStatComponentDetailBean> getDetailBeansList() {
        return detailBeansList;
    }

    public void setDetailBeansList(ArrayList<ActivityStatComponentDetailBean> detailBeansList) {
        this.detailBeansList = detailBeansList;
    }
}