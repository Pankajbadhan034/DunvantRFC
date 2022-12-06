package com.dunvant.application.beans;

import java.io.Serializable;

public class ChildDetailScoreBean implements Serializable{

    private String parentId;
    private String performanceSubElementId;
    private String subElementName;
    private String scoreSubElementId;
    private String score;
    private String type;
    private String comments;
    private String suggestions;
    private String scorePercentage;
    private String colorCode;
    private boolean askConfirm;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPerformanceSubElementId() {
        return performanceSubElementId;
    }

    public void setPerformanceSubElementId(String performanceSubElementId) {
        this.performanceSubElementId = performanceSubElementId;
    }

    public String getSubElementName() {
        return subElementName;
    }

    public void setSubElementName(String subElementName) {
        this.subElementName = subElementName;
    }

    public String getScoreSubElementId() {
        return scoreSubElementId;
    }

    public void setScoreSubElementId(String scoreSubElementId) {
        this.scoreSubElementId = scoreSubElementId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(String scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public boolean isAskConfirm() {
        return askConfirm;
    }

    public void setAskConfirm(boolean askConfirm) {
        this.askConfirm = askConfirm;
    }
}