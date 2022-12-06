package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderSessionBean implements Serializable{

    private String sessionId;
    private String coachingProgramName;
    private String termsName;
    private String locationsName;
    private String isTrial;
    private String showStartTime;
    private String showEndTime;
    private String groupName;
    private String sessionCost;

    private ArrayList<ChildBean> childrenList = new ArrayList<>();
    private ArrayList<AcademySessionDateBean> bookingDetailsList = new ArrayList<>();

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

    public String getTermsName() {
        return termsName;
    }

    public void setTermsName(String termsName) {
        this.termsName = termsName;
    }

    public String getLocationsName() {
        return locationsName;
    }

    public void setLocationsName(String locationsName) {
        this.locationsName = locationsName;
    }

    public String getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSessionCost() {
        return sessionCost;
    }

    public void setSessionCost(String sessionCost) {
        this.sessionCost = sessionCost;
    }

    public ArrayList<ChildBean> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(ArrayList<ChildBean> childrenList) {
        this.childrenList = childrenList;
    }

    public ArrayList<AcademySessionDateBean> getBookingDetailsList() {
        return bookingDetailsList;
    }

    public void setBookingDetailsList(ArrayList<AcademySessionDateBean> bookingDetailsList) {
        this.bookingDetailsList = bookingDetailsList;
    }
}