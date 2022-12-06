package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchTimeSlotBean implements Serializable{

    private String timeSlot;
    private boolean selected;
    private String pitchId;

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPitchId() {
        return pitchId;
    }

    public void setPitchId(String pitchId) {
        this.pitchId = pitchId;
    }
}