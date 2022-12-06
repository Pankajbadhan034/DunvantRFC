package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailedScoreDataBean implements Serializable{

    private String childName;
    private String sessionsId;
    private String locationsName;
    private String termName;
    private String day;
    private String groupName;
    private String scoreId;
    private String performanceElementId;
    private String elementName;
    private String childId;
    private String lowLabel;
    private String middleLabel;
    private String topLabel;
    private String dayLabel;

    private ArrayList<ChildDetailScoreBean> childDetailScoresListBean = new ArrayList<>();

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getSessionsId() {
        return sessionsId;
    }

    public void setSessionsId(String sessionsId) {
        this.sessionsId = sessionsId;
    }

    public String getLocationsName() {
        return locationsName;
    }

    public void setLocationsName(String locationsName) {
        this.locationsName = locationsName;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getPerformanceElementId() {
        return performanceElementId;
    }

    public void setPerformanceElementId(String performanceElementId) {
        this.performanceElementId = performanceElementId;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getLowLabel() {
        return lowLabel;
    }

    public void setLowLabel(String lowLabel) {
        this.lowLabel = lowLabel;
    }

    public String getMiddleLabel() {
        return middleLabel;
    }

    public void setMiddleLabel(String middleLabel) {
        this.middleLabel = middleLabel;
    }

    public String getTopLabel() {
        return topLabel;
    }

    public void setTopLabel(String topLabel) {
        this.topLabel = topLabel;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public ArrayList<ChildDetailScoreBean> getChildDetailScoresListBean() {
        return childDetailScoresListBean;
    }

    public void setChildDetailScoresListBean(ArrayList<ChildDetailScoreBean> childDetailScoresListBean) {
        this.childDetailScoresListBean = childDetailScoresListBean;
    }
}