package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class PitchDateBean implements Serializable{

    private String date;
    private ArrayList<PitchTimeSlotBean> timeSlots = new ArrayList<>();
    private boolean selected;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<PitchTimeSlotBean> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(ArrayList<PitchTimeSlotBean> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}