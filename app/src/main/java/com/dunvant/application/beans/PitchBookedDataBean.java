package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchBookedDataBean implements Serializable{

    private String ordersId;
    private String bookingDate;
    private String bookedFromTime;
    private String bookedToTime;

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookedFromTime() {
        return bookedFromTime;
    }

    public void setBookedFromTime(String bookedFromTime) {
        this.bookedFromTime = bookedFromTime;
    }

    public String getBookedToTime() {
        return bookedToTime;
    }

    public void setBookedToTime(String bookedToTime) {
        this.bookedToTime = bookedToTime;
    }
}