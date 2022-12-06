package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CoachPlayerDetailStatsBean implements Serializable {
    String league_name;
    ArrayList<StatsLeagueBean> statsLeagueBeanArrayList;

    public ArrayList<StatsLeagueBean> getStatsLeagueBeanArrayList() {
        return statsLeagueBeanArrayList;
    }

    public void setStatsLeagueBeanArrayList(ArrayList<StatsLeagueBean> statsLeagueBeanArrayList) {
        this.statsLeagueBeanArrayList = statsLeagueBeanArrayList;
    }

    public String getLeague_name() {
        return league_name;
    }

    public void setLeague_name(String league_name) {
        this.league_name = league_name;
    }


}
