package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ChildScoreBean implements Serializable{

    private String scoresId;
    private String score;
    private String performanceElementId;
    private String parentId;
    private String elementName;
    private String videoUrl;
    private ArrayList<ChildDetailScoreBean> detailedScores = new ArrayList<>();
    private boolean scoreLocked;
    private String coachId;
    private String colorCode;
    private String performancePercentage;
    private String areaOfDevelopment;

    public String getScoresId() {
        return scoresId;
    }

    public void setScoresId(String scoresId) {
        this.scoresId = scoresId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPerformanceElementId() {
        return performanceElementId;
    }

    public void setPerformanceElementId(String performanceElementId) {
        this.performanceElementId = performanceElementId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public ArrayList<ChildDetailScoreBean> getDetailedScores() {
        return detailedScores;
    }

    public void setDetailedScores(ArrayList<ChildDetailScoreBean> detailedScores) {
        this.detailedScores = detailedScores;
    }

    public boolean isScoreLocked() {
        return scoreLocked;
    }

    public void setScoreLocked(boolean scoreLocked) {
        this.scoreLocked = scoreLocked;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getPerformancePercentage() {
        return performancePercentage;
    }

    public void setPerformancePercentage(String performancePercentage) {
        this.performancePercentage = performancePercentage;
    }

    public String getAreaOfDevelopment() {
        return areaOfDevelopment;
    }

    public void setAreaOfDevelopment(String areaOfDevelopment) {
        this.areaOfDevelopment = areaOfDevelopment;
    }
}