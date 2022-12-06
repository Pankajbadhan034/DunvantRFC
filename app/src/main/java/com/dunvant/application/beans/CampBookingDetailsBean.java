package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CampBookingDetailsBean implements Serializable{

    private String childName;
    private ArrayList<BookingDateBean> bookingDatesList;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public ArrayList<BookingDateBean> getBookingDatesList() {
        return bookingDatesList;
    }

    public void setBookingDatesList(ArrayList<BookingDateBean> bookingDatesList) {
        this.bookingDatesList = bookingDatesList;
    }
}