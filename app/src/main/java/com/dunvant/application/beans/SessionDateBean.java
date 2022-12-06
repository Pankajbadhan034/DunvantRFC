package com.dunvant.application.beans;

import java.io.Serializable;

public class SessionDateBean implements Serializable{

    private String sessionDate;
    private String showSessionDate;
    private String dayLabel;
    private boolean canMarkAttendance;
    private boolean selected;

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getShowSessionDate() {
        return showSessionDate;
    }

    public void setShowSessionDate(String showSessionDate) {
        this.showSessionDate = showSessionDate;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public boolean isCanMarkAttendance() {
        return canMarkAttendance;
    }

    public void setCanMarkAttendance(boolean canMarkAttendance) {
        this.canMarkAttendance = canMarkAttendance;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}