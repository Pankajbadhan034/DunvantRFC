package com.dunvant.application.beans;

import java.io.Serializable;

public class AcademySessionDetailBean implements Serializable{

    private String sessionDetailId;
    private String sessionDetailTitle;
    private String sessionId;
    private String groupName;
    private int fromAge;
    private int toAge;
    private String day;
    private String fromDate;
    private String toDate;
    private String showFromDate;
    private String showToDate;
    private String numberOfWeeks;
    private String startTime;
    private String endTime;
    private String showStartTime;
    private String showEndTime;
    private String numberOfHours;
    private String cost;
    private String isSelectiveAllowed;
    private String dayLabel;
    private String coachName;
    private boolean thresholdCrossed;
    private String coachingProgramName;
    private String sessionGender;
    private String sessionGenderLabel;
    private boolean bookingClosed;
    private String sessionExpiresInLabel;

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getShowFromDate() {
        return showFromDate;
    }

    public void setShowFromDate(String showFromDate) {
        this.showFromDate = showFromDate;
    }

    public String getShowToDate() {
        return showToDate;
    }

    public void setShowToDate(String showToDate) {
        this.showToDate = showToDate;
    }

    public String getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public void setNumberOfWeeks(String numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
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

    public String getIsSelectiveAllowed() {
        return isSelectiveAllowed;
    }

    public void setIsSelectiveAllowed(String isSelectiveAllowed) {
        this.isSelectiveAllowed = isSelectiveAllowed;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public int getFromAge() {
        return fromAge;
    }

    public void setFromAge(int fromAge) {
        this.fromAge = fromAge;
    }

    public int getToAge() {
        return toAge;
    }

    public void setToAge(int toAge) {
        this.toAge = toAge;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public boolean isThresholdCrossed() {
        return thresholdCrossed;
    }

    public void setThresholdCrossed(boolean thresholdCrossed) {
        this.thresholdCrossed = thresholdCrossed;
    }

    public String getCoachingProgramName() {
        return coachingProgramName;
    }

    public void setCoachingProgramName(String coachingProgramName) {
        this.coachingProgramName = coachingProgramName;
    }

    public String getSessionGender() {
        return sessionGender;
    }

    public void setSessionGender(String sessionGender) {
        this.sessionGender = sessionGender;
    }

    public String getSessionGenderLabel() {
        return sessionGenderLabel;
    }

    public void setSessionGenderLabel(String sessionGenderLabel) {
        this.sessionGenderLabel = sessionGenderLabel;
    }

    public boolean isBookingClosed() {
        return bookingClosed;
    }

    public void setBookingClosed(boolean bookingClosed) {
        this.bookingClosed = bookingClosed;
    }

    public String getSessionExpiresInLabel() {
        return sessionExpiresInLabel;
    }

    public void setSessionExpiresInLabel(String sessionExpiresInLabel) {
        this.sessionExpiresInLabel = sessionExpiresInLabel;
    }
}