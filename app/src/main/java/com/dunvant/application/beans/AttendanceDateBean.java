package com.dunvant.application.beans;

import java.io.Serializable;

public class AttendanceDateBean implements Serializable{

    private String bookedSessionDate;
    private String showBookedSessionDate;
    private String status;

    public String getBookedSessionDate() {
        return bookedSessionDate;
    }

    public void setBookedSessionDate(String bookedSessionDate) {
        this.bookedSessionDate = bookedSessionDate;
    }

    public String getShowBookedSessionDate() {
        return showBookedSessionDate;
    }

    public void setShowBookedSessionDate(String showBookedSessionDate) {
        this.showBookedSessionDate = showBookedSessionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}