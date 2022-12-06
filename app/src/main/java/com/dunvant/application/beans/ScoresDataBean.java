package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoresDataBean implements Serializable{

    private String sessionsId;
    private String childId;
    private String childName;
    private String locationName;
    private String termName;
    private String termId;
    private String day;
    private String dayLabel;
    private String groupName;
    private String enrollmentDate;
    private String showEnrollmentDate;
    private String totalScore;
    private ArrayList<ChildScoreBean> childrenScoresListing = new ArrayList<>();
    private String latestScoreDate;
    private boolean attendanceEnable;

    public String getSessionsId() {
        return sessionsId;
    }

    public void setSessionsId(String sessionsId) {
        this.sessionsId = sessionsId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getShowEnrollmentDate() {
        return showEnrollmentDate;
    }

    public void setShowEnrollmentDate(String showEnrollmentDate) {
        this.showEnrollmentDate = showEnrollmentDate;
    }

    public ArrayList<ChildScoreBean> getChildrenScoresListing() {
        return childrenScoresListing;
    }

    public void setChildrenScoresListing(ArrayList<ChildScoreBean> childrenScoresListing) {
        this.childrenScoresListing = childrenScoresListing;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getLatestScoreDate() {
        return latestScoreDate;
    }

    public void setLatestScoreDate(String latestScoreDate) {
        this.latestScoreDate = latestScoreDate;
    }

    public boolean isAttendanceEnable() {
        return attendanceEnable;
    }

    public void setAttendanceEnable(boolean attendanceEnable) {
        this.attendanceEnable = attendanceEnable;
    }
}