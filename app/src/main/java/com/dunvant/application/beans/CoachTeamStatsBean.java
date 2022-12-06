package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CoachTeamStatsBean implements Serializable {
    String team_id;
    String match_id;
    String player_id;
    ArrayList<CoachPlayerStatsbean>coachPlayerStatsbeanArrayList;

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public ArrayList<CoachPlayerStatsbean> getCoachPlayerStatsbeanArrayList() {
        return coachPlayerStatsbeanArrayList;
    }

    public void setCoachPlayerStatsbeanArrayList(ArrayList<CoachPlayerStatsbean> coachPlayerStatsbeanArrayList) {
        this.coachPlayerStatsbeanArrayList = coachPlayerStatsbeanArrayList;
    }
}
