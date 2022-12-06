package com.dunvant.application.beans;

import java.io.Serializable;

public class DatesResultBean implements Serializable{

    private String sessionId;
    private String coachingProgramId;
    private String coachingProgramName;
    private String termsId;
    private String termsName;
    private String locationId;
    private String locationName;
    private String ageGroupId;
    private String groupName;
    private int fromAge;
    private int toAge;
    private int day;
    private String usersId;
    private String childName;
    private String ordersIds;
    private String bookingDates;
    private String dayLabel;
    private String movedData;
    private String isTrial;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCoachingProgramId() {
        return coachingProgramId;
    }

    public void setCoachingProgramId(String coachingProgramId) {
        this.coachingProgramId = coachingProgramId;
    }

    public String getCoachingProgramName() {
        return coachingProgramName;
    }

    public void setCoachingProgramName(String coachingProgramName) {
        this.coachingProgramName = coachingProgramName;
    }

    public String getTermsId() {
        return termsId;
    }

    public void setTermsId(String termsId) {
        this.termsId = termsId;
    }

    public String getTermsName() {
        return termsName;
    }

    public void setTermsName(String termsName) {
        this.termsName = termsName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAgeGroupId() {
        return ageGroupId;
    }

    public void setAgeGroupId(String ageGroupId) {
        this.ageGroupId = ageGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getOrdersIds() {
        return ordersIds;
    }

    public void setOrdersIds(String ordersIds) {
        this.ordersIds = ordersIds;
    }

    public String getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(String bookingDates) {
        this.bookingDates = bookingDates;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public String getMovedData() {
        return movedData;
    }

    public void setMovedData(String movedData) {
        this.movedData = movedData;
    }

    public String getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
    }
}