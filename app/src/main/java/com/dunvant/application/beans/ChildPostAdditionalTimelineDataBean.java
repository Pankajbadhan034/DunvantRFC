package com.dunvant.application.beans;

import java.io.Serializable;

/**
 * Created by DEVLABS\pbadhan on 6/7/17.
 */
public class ChildPostAdditionalTimelineDataBean implements Serializable {
    String dataElementName;
    String dataElementPerformancePercentage;
    String dataElementColorCode;
    String dataElementScore;
    String dataElementTotalScore;
    String dataElementSessionId;

    String activityName;
    String total;
    String average;
    String levelName;
    String colorCode;
    String sessionId;
    String reportDate;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

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

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getDataElementName() {
        return dataElementName;
    }

    public void setDataElementName(String dataElementName) {
        this.dataElementName = dataElementName;
    }

    public String getDataElementPerformancePercentage() {
        return dataElementPerformancePercentage;
    }

    public void setDataElementPerformancePercentage(String dataElementPerformancePercentage) {
        this.dataElementPerformancePercentage = dataElementPerformancePercentage;
    }

    public String getDataElementColorCode() {
        return dataElementColorCode;
    }

    public void setDataElementColorCode(String dataElementColorCode) {
        this.dataElementColorCode = dataElementColorCode;
    }

    public String getDataElementScore() {
        return dataElementScore;
    }

    public void setDataElementScore(String dataElementScore) {
        this.dataElementScore = dataElementScore;
    }

    public String getDataElementTotalScore() {
        return dataElementTotalScore;
    }

    public void setDataElementTotalScore(String dataElementTotalScore) {
        this.dataElementTotalScore = dataElementTotalScore;
    }

    public String getDataElementSessionId() {
        return dataElementSessionId;
    }

    public void setDataElementSessionId(String dataElementSessionId) {
        this.dataElementSessionId = dataElementSessionId;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
}
