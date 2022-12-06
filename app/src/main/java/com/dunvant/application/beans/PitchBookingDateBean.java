package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchBookingDateBean implements Serializable {

    private String showBookingDate;
    private String bookingDate;
    private String fromTime;
    private String toTime;
    private String time;
    private String interval;
    private String amount;

    public String getShowBookingDate() {
        return showBookingDate;
    }

    public void setShowBookingDate(String showBookingDate) {
        this.showBookingDate = showBookingDate;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}