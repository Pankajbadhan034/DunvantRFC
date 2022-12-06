package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamDetailBean implements Serializable {
    String teamId;
    String matchId;
    String playerId;
    String name;
    String imageUrl;
    String image;

    ArrayList<TeamStatsBean>teamStatsBeanArrayList;



    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<TeamStatsBean> getTeamStatsBeanArrayList() {
        return teamStatsBeanArrayList;
    }

    public void setTeamStatsBeanArrayList(ArrayList<TeamStatsBean> teamStatsBeanArrayList) {
        this.teamStatsBeanArrayList = teamStatsBeanArrayList;
    }
}
