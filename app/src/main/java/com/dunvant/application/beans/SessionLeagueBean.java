package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class SessionLeagueBean implements Serializable {
    String teamId;
    String session_name;
    ArrayList<StatsLeagueBean> statsLeagueBeanArrayList;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public ArrayList<StatsLeagueBean> getStatsLeagueBeanArrayList() {
        return statsLeagueBeanArrayList;
    }

    public void setStatsLeagueBeanArrayList(ArrayList<StatsLeagueBean> statsLeagueBeanArrayList) {
        this.statsLeagueBeanArrayList = statsLeagueBeanArrayList;
    }
}
