package com.dunvant.application.beans;

import java.io.Serializable;

public class CoachLeagueDetailOneUpcomingMatchBean implements Serializable {
    String imageUrl;
    String matchId;
    String team1;
    String team1Name;
    String team1Logo;
    String team2;
    String team2Name;
    String team2Logo;
    String matchDate;
    String matchTime;
    String ground_name;
    String counterTime;
    String counterDate;
    String team1id;
    String team2id;

    public String getTeam1id() {
        return team1id;
    }

    public void setTeam1id(String team1id) {
        this.team1id = team1id;
    }

    public String getTeam2id() {
        return team2id;
    }

    public void setTeam2id(String team2id) {
        this.team2id = team2id;
    }


    public String getCounterDate() {
        return counterDate;
    }

    public void setCounterDate(String counterDate) {
        this.counterDate = counterDate;
    }

    public String getCounterTime() {
        return counterTime;
    }

    public void setCounterTime(String counterTime) {
        this.counterTime = counterTime;
    }

    public String getGround_name() {
        return ground_name;
    }

    public void setGround_name(String ground_name) {
        this.ground_name = ground_name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam1Logo() {
        return team1Logo;
    }

    public void setTeam1Logo(String team1Logo) {
        this.team1Logo = team1Logo;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getTeam2Logo() {
        return team2Logo;
    }

    public void setTeam2Logo(String team2Logo) {
        this.team2Logo = team2Logo;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }
}
