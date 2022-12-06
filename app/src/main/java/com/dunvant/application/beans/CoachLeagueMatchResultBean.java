package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CoachLeagueMatchResultBean implements Serializable {
    String id;
    String academy_id;
    String scheduler_id;
    String match_name;
    String description;
    String round;
    String team_1;
    String team_2;
    String team_1_result;
    String team_2_result;
    String team_1_score;
    String team_2_score;
    String team1_ref_id;
    String team2_ref_id;
    String match_date;
    String match_time;
    String match_date_time;
    String season;
    String match_round;
    String match_type;
    String ground;
    String file_name;
    String url;
    String created;
    String modified;
    String state;
    String image_url;
    String league_id;
    String season_id;
    String ground_id;
    String address;
    String team_1_logo;
    String team_2_logo;
    String match_date_formatted;
    String match_time_formatted;
    ArrayList<ConfigueMatchListBean> configueMatchListArrayList;
    ArrayList<CoachTeamResultBean> coachTeamResult1BeanArrayList;
    ArrayList<CoachTeamResultBean> coachTeamResult2BeanArrayList;
    ArrayList<CoachTeamStatsBean> coachTeam1StatsBeanArrayList;
    ArrayList<CoachTeamStatsBean> coachTeam2StatsBeanArrayList;
    String team1Name;
    String team2Name;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(String academy_id) {
        this.academy_id = academy_id;
    }

    public String getScheduler_id() {
        return scheduler_id;
    }

    public void setScheduler_id(String scheduler_id) {
        this.scheduler_id = scheduler_id;
    }

    public String getMatch_name() {
        return match_name;
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getTeam_1() {
        return team_1;
    }

    public void setTeam_1(String team_1) {
        this.team_1 = team_1;
    }

    public String getTeam_2() {
        return team_2;
    }

    public void setTeam_2(String team_2) {
        this.team_2 = team_2;
    }

    public String getTeam_1_result() {
        return team_1_result;
    }

    public void setTeam_1_result(String team_1_result) {
        this.team_1_result = team_1_result;
    }

    public String getTeam_2_result() {
        return team_2_result;
    }

    public void setTeam_2_result(String team_2_result) {
        this.team_2_result = team_2_result;
    }

    public String getTeam_1_score() {
        return team_1_score;
    }

    public void setTeam_1_score(String team_1_score) {
        this.team_1_score = team_1_score;
    }

    public String getTeam_2_score() {
        return team_2_score;
    }

    public void setTeam_2_score(String team_2_score) {
        this.team_2_score = team_2_score;
    }

    public String getTeam1_ref_id() {
        return team1_ref_id;
    }

    public void setTeam1_ref_id(String team1_ref_id) {
        this.team1_ref_id = team1_ref_id;
    }

    public String getTeam2_ref_id() {
        return team2_ref_id;
    }

    public void setTeam2_ref_id(String team2_ref_id) {
        this.team2_ref_id = team2_ref_id;
    }

    public String getMatch_date() {
        return match_date;
    }

    public void setMatch_date(String match_date) {
        this.match_date = match_date;
    }

    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public String getMatch_date_time() {
        return match_date_time;
    }

    public void setMatch_date_time(String match_date_time) {
        this.match_date_time = match_date_time;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getMatch_round() {
        return match_round;
    }

    public void setMatch_round(String match_round) {
        this.match_round = match_round;
    }

    public String getMatch_type() {
        return match_type;
    }

    public void setMatch_type(String match_type) {
        this.match_type = match_type;
    }

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLeague_id() {
        return league_id;
    }

    public void setLeague_id(String league_id) {
        this.league_id = league_id;
    }

    public String getSeason_id() {
        return season_id;
    }

    public void setSeason_id(String season_id) {
        this.season_id = season_id;
    }

    public String getGround_id() {
        return ground_id;
    }

    public void setGround_id(String ground_id) {
        this.ground_id = ground_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTeam_1_logo() {
        return team_1_logo;
    }

    public void setTeam_1_logo(String team_1_logo) {
        this.team_1_logo = team_1_logo;
    }

    public String getTeam_2_logo() {
        return team_2_logo;
    }

    public void setTeam_2_logo(String team_2_logo) {
        this.team_2_logo = team_2_logo;
    }

    public String getMatch_date_formatted() {
        return match_date_formatted;
    }

    public void setMatch_date_formatted(String match_date_formatted) {
        this.match_date_formatted = match_date_formatted;
    }

    public String getMatch_time_formatted() {
        return match_time_formatted;
    }

    public void setMatch_time_formatted(String match_time_formatted) {
        this.match_time_formatted = match_time_formatted;
    }

    public ArrayList<ConfigueMatchListBean> getConfigueMatchListArrayList() {
        return configueMatchListArrayList;
    }

    public void setConfigueMatchListArrayList(ArrayList<ConfigueMatchListBean> configueMatchListArrayList) {
        this.configueMatchListArrayList = configueMatchListArrayList;
    }

    public ArrayList<CoachTeamResultBean> getCoachTeamResult1BeanArrayList() {
        return coachTeamResult1BeanArrayList;
    }

    public void setCoachTeamResult1BeanArrayList(ArrayList<CoachTeamResultBean> coachTeamResult1BeanArrayList) {
        this.coachTeamResult1BeanArrayList = coachTeamResult1BeanArrayList;
    }

    public ArrayList<CoachTeamResultBean> getCoachTeamResult2BeanArrayList() {
        return coachTeamResult2BeanArrayList;
    }

    public void setCoachTeamResult2BeanArrayList(ArrayList<CoachTeamResultBean> coachTeamResult2BeanArrayList) {
        this.coachTeamResult2BeanArrayList = coachTeamResult2BeanArrayList;
    }

    public ArrayList<CoachTeamStatsBean> getCoachTeam1StatsBeanArrayList() {
        return coachTeam1StatsBeanArrayList;
    }

    public void setCoachTeam1StatsBeanArrayList(ArrayList<CoachTeamStatsBean> coachTeam1StatsBeanArrayList) {
        this.coachTeam1StatsBeanArrayList = coachTeam1StatsBeanArrayList;
    }

    public ArrayList<CoachTeamStatsBean> getCoachTeam2StatsBeanArrayList() {
        return coachTeam2StatsBeanArrayList;
    }

    public void setCoachTeam2StatsBeanArrayList(ArrayList<CoachTeamStatsBean> coachTeam2StatsBeanArrayList) {
        this.coachTeam2StatsBeanArrayList = coachTeam2StatsBeanArrayList;
    }
}
