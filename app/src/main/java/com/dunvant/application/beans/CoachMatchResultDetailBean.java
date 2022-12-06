package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CoachMatchResultDetailBean implements Serializable {
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
    String team1Score;
    String team2Score;
    String address;
    String leaqueId;
    ArrayList<TableHeadingBean> tableHeadingBeanArrayList;
    ArrayList<TeamDetailBean> teamDetail1BeanArrayList;
    ArrayList<TeamDetailBean> teamDetail2BeanArrayList;
    ArrayList<TeamTotalStatsBean>team1TotalStatsBeanArrayList;
    ArrayList<TeamTotalStatsBean>team2TotalStatsBeanArrayList;


    public ArrayList<TeamTotalStatsBean> getTeam1TotalStatsBeanArrayList() {
        return team1TotalStatsBeanArrayList;
    }

    public void setTeam1TotalStatsBeanArrayList(ArrayList<TeamTotalStatsBean> team1TotalStatsBeanArrayList) {
        this.team1TotalStatsBeanArrayList = team1TotalStatsBeanArrayList;
    }

    public ArrayList<TeamTotalStatsBean> getTeam2TotalStatsBeanArrayList() {
        return team2TotalStatsBeanArrayList;
    }

    public void setTeam2TotalStatsBeanArrayList(ArrayList<TeamTotalStatsBean> team2TotalStatsBeanArrayList) {
        this.team2TotalStatsBeanArrayList = team2TotalStatsBeanArrayList;
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

    public String getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(String team1Score) {
        this.team1Score = team1Score;
    }

    public String getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(String team2Score) {
        this.team2Score = team2Score;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLeaqueId() {
        return leaqueId;
    }

    public void setLeaqueId(String leaqueId) {
        this.leaqueId = leaqueId;
    }

    public ArrayList<TableHeadingBean> getTableHeadingBeanArrayList() {
        return tableHeadingBeanArrayList;
    }

    public void setTableHeadingBeanArrayList(ArrayList<TableHeadingBean> tableHeadingBeanArrayList) {
        this.tableHeadingBeanArrayList = tableHeadingBeanArrayList;
    }

    public ArrayList<TeamDetailBean> getTeamDetail1BeanArrayList() {
        return teamDetail1BeanArrayList;
    }

    public void setTeamDetail1BeanArrayList(ArrayList<TeamDetailBean> teamDetail1BeanArrayList) {
        this.teamDetail1BeanArrayList = teamDetail1BeanArrayList;
    }

    public ArrayList<TeamDetailBean> getTeamDetail2BeanArrayList() {
        return teamDetail2BeanArrayList;
    }

    public void setTeamDetail2BeanArrayList(ArrayList<TeamDetailBean> teamDetail2BeanArrayList) {
        this.teamDetail2BeanArrayList = teamDetail2BeanArrayList;
    }


}
