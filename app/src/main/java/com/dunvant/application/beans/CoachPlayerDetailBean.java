package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CoachPlayerDetailBean implements Serializable {
    String id;
    String academy_id;
    String team_id;
    String squad_number;
    String nationality;
    String position;
    String team;
    String league;
    String seasons;
    String height;
    String weight;
    String name;
    String dob;
    String description;
    String image;
    String state;
    String created;
    String modified;
    String dob_formatted;
    String team_logo;
    String matches_played;
    String score;

    String player_id;

    ArrayList<CoachPlayerDetailStatsBean> coachPlayerDetailStatsBeanArrayList;
    ArrayList<CoachLeagueDetailCareerBeen> coachLeagueDetailCareerBeenArrayList;

    String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getSquad_number() {
        return squad_number;
    }

    public void setSquad_number(String squad_number) {
        this.squad_number = squad_number;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getSeasons() {
        return seasons;
    }

    public void setSeasons(String seasons) {
        this.seasons = seasons;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getDob_formatted() {
        return dob_formatted;
    }

    public void setDob_formatted(String dob_formatted) {
        this.dob_formatted = dob_formatted;
    }

    public String getTeam_logo() {
        return team_logo;
    }

    public void setTeam_logo(String team_logo) {
        this.team_logo = team_logo;
    }

    public String getMatches_played() {
        return matches_played;
    }

    public void setMatches_played(String matches_played) {
        this.matches_played = matches_played;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public ArrayList<CoachPlayerDetailStatsBean> getCoachPlayerDetailStatsBeanArrayList() {
        return coachPlayerDetailStatsBeanArrayList;
    }

    public void setCoachPlayerDetailStatsBeanArrayList(ArrayList<CoachPlayerDetailStatsBean> coachPlayerDetailStatsBeanArrayList) {
        this.coachPlayerDetailStatsBeanArrayList = coachPlayerDetailStatsBeanArrayList;
    }

    public ArrayList<CoachLeagueDetailCareerBeen> getCoachLeagueDetailCareerBeenArrayList() {
        return coachLeagueDetailCareerBeenArrayList;
    }

    public void setCoachLeagueDetailCareerBeenArrayList(ArrayList<CoachLeagueDetailCareerBeen> coachLeagueDetailCareerBeenArrayList) {
        this.coachLeagueDetailCareerBeenArrayList = coachLeagueDetailCareerBeenArrayList;
    }
}
