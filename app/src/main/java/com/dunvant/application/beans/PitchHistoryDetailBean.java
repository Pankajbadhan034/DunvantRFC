package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class PitchHistoryDetailBean implements Serializable{

    private String pitchName;
    private String pitchId;
    private String locationName;
    private String pitchPrices;
//    private String pricesDisplay;
    private ArrayList<PitchBookingBean> bookingsList = new ArrayList<>();

    public String getPitchName() {
        return pitchName;
    }

    public void setPitchName(String pitchName) {
        this.pitchName = pitchName;
    }

    public String getPitchId() {
        return pitchId;
    }

    public void setPitchId(String pitchId) {
        this.pitchId = pitchId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPitchPrices() {
        return pitchPrices;
    }

    public void setPitchPrices(String pitchPrices) {
        this.pitchPrices = pitchPrices;
    }

    /*public String getPricesDisplay() {
        return pricesDisplay;
    }

    public void setPricesDisplay(String pricesDisplay) {
        this.pricesDisplay = pricesDisplay;
    }*/

    public ArrayList<PitchBookingBean> getBookingsList() {
        return bookingsList;
    }

    public void setBookingsList(ArrayList<PitchBookingBean> bookingsList) {
        this.bookingsList = bookingsList;
    }
}