package com.dunvant.application.beans;

import java.io.Serializable;

public class CoachAssignedLeagueMatchesBean implements Serializable {
    String imageUrl;
    String team1Logo;
    String team2Logo;
    String id;
    String matchDateFormatted;
    String matchTime;
    String team1Name;
    String team2Name;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTeam1Logo() {
        return team1Logo;
    }

    public void setTeam1Logo(String team1Logo) {
        this.team1Logo = team1Logo;
    }

    public String getTeam2Logo() {
        return team2Logo;
    }

    public void setTeam2Logo(String team2Logo) {
        this.team2Logo = team2Logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchDateFormatted() {
        return matchDateFormatted;
    }

    public void setMatchDateFormatted(String matchDateFormatted) {
        this.matchDateFormatted = matchDateFormatted;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }
}
