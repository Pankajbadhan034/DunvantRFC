package com.dunvant.application.beans;

import java.io.Serializable;

public class LeagueBean implements Serializable{

    private String leagueId;
    private String academiesId;
    private String leagueName;
    private String showFromDate;
    private String showToDate;
    private String leagueDescription;
    private String leagueState;
    private String fileTitle;
    private String filePath;

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getAcademiesId() {
        return academiesId;
    }

    public void setAcademiesId(String academiesId) {
        this.academiesId = academiesId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
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

    public String getLeagueDescription() {
        return leagueDescription;
    }

    public void setLeagueDescription(String leagueDescription) {
        this.leagueDescription = leagueDescription;
    }

    public String getLeagueState() {
        return leagueState;
    }

    public void setLeagueState(String leagueState) {
        this.leagueState = leagueState;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}