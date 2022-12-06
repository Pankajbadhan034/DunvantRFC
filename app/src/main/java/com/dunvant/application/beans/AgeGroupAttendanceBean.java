package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class AgeGroupAttendanceBean implements Serializable{

    private String totalScore;
    private String sessionsId;
    private String ageGroupId;
    private String groupName;
    private String termsName;
    private String usersId;
    private String childName;
    private String bookingDates;
    private ArrayList<AttendanceDateBean> attendanceDatesList = new ArrayList<>();
    private String attendedSessionsCount;
    private String totalSessionsCount;
    private String remainingSessionsCount;

    // Adding following new score related parameters
    private String day;
    private String bookingDate;
    private String ageGroupsId;
    private String termFromDate;
    private String termToDate;
    private String dayLabel;
    private String scoreId;
    private String reportDate;

//    private String childId;
//    private String sessionId;
//    private String childName;
//    private String groupName;
//    private String termsName;

    public String getSessionsId() {
        return sessionsId;
    }

    public void setSessionsId(String sessionsId) {
        this.sessionsId = sessionsId;
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

    public String getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(String bookingDates) {
        this.bookingDates = bookingDates;
    }

    public ArrayList<AttendanceDateBean> getAttendanceDatesList() {
        return attendanceDatesList;
    }

    public void setAttendanceDatesList(ArrayList<AttendanceDateBean> attendanceDatesList) {
        this.attendanceDatesList = attendanceDatesList;
    }

    public String getAttendedSessionsCount() {
        return attendedSessionsCount;
    }

    public void setAttendedSessionsCount(String attendedSessionsCount) {
        this.attendedSessionsCount = attendedSessionsCount;
    }

    public String getTotalSessionsCount() {
        return totalSessionsCount;
    }

    public void setTotalSessionsCount(String totalSessionsCount) {
        this.totalSessionsCount = totalSessionsCount;
    }

    public String getRemainingSessionsCount() {
        return remainingSessionsCount;
    }

    public void setRemainingSessionsCount(String remainingSessionsCount) {
        this.remainingSessionsCount = remainingSessionsCount;
    }

    public String getTermsName() {
        return termsName;
    }

    public void setTermsName(String termsName) {
        this.termsName = termsName;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getAgeGroupsId() {
        return ageGroupsId;
    }

    public void setAgeGroupsId(String ageGroupsId) {
        this.ageGroupsId = ageGroupsId;
    }

    public String getTermFromDate() {
        return termFromDate;
    }

    public void setTermFromDate(String termFromDate) {
        this.termFromDate = termFromDate;
    }

    public String getTermToDate() {
        return termToDate;
    }

    public void setTermToDate(String termToDate) {
        this.termToDate = termToDate;
    }

    /*public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }*/

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
}