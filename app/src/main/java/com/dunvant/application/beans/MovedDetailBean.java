package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class MovedDetailBean implements Serializable{

    private String sessionsId;
    private String coachingProgramsId;
    private String coachingProgramsName;
    private String termsId;
    private String termsName;
    private String locationsId;
    private String locationsName;
    private String ageGroupsId;
    private String groupName;
    private String fromAge;
    private String toAge;
    private String day;
    private String usersId;
    private String childName;
    private String ordersId;
    private String bookingDates;
    private String startTimeFormatted;
    private String endTimeFormatted;
    private String title;
    private String dayLabel;

    private ArrayList<AttendanceDateBean> attendanceListing;

    public String getSessionsId() {
        return sessionsId;
    }

    public void setSessionsId(String sessionsId) {
        this.sessionsId = sessionsId;
    }

    public String getCoachingProgramsId() {
        return coachingProgramsId;
    }

    public void setCoachingProgramsId(String coachingProgramsId) {
        this.coachingProgramsId = coachingProgramsId;
    }

    public String getCoachingProgramsName() {
        return coachingProgramsName;
    }

    public void setCoachingProgramsName(String coachingProgramsName) {
        this.coachingProgramsName = coachingProgramsName;
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

    public String getLocationsId() {
        return locationsId;
    }

    public void setLocationsId(String locationsId) {
        this.locationsId = locationsId;
    }

    public String getLocationsName() {
        return locationsName;
    }

    public void setLocationsName(String locationsName) {
        this.locationsName = locationsName;
    }

    public String getAgeGroupsId() {
        return ageGroupsId;
    }

    public void setAgeGroupsId(String ageGroupsId) {
        this.ageGroupsId = ageGroupsId;
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

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(String bookingDates) {
        this.bookingDates = bookingDates;
    }

    public String getStartTimeFormatted() {
        return startTimeFormatted;
    }

    public void setStartTimeFormatted(String startTimeFormatted) {
        this.startTimeFormatted = startTimeFormatted;
    }

    public String getEndTimeFormatted() {
        return endTimeFormatted;
    }

    public void setEndTimeFormatted(String endTimeFormatted) {
        this.endTimeFormatted = endTimeFormatted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public ArrayList<AttendanceDateBean> getAttendanceListing() {
        return attendanceListing;
    }

    public void setAttendanceListing(ArrayList<AttendanceDateBean> attendanceListing) {
        this.attendanceListing = attendanceListing;
    }
}