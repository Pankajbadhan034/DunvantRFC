package com.dunvant.application.beans;

import java.io.Serializable;

public class CampWeekBean implements Serializable{

    private String weekName;
    private String weekDates;
    private int seats;
    private boolean selected;

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getWeekDates() {
        return weekDates;
    }

    public void setWeekDates(String weekDates) {
        this.weekDates = weekDates;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}