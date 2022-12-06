package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class PitchBookingSlotsDataBean implements Serializable{

//    private String pitchId;
    private String locationId;
    private ArrayList<PitchDateBean> pitchDateBeenList = new ArrayList<>();
    private boolean fullPitch;

    /*public String getPitchId() {
        return pitchId;
    }

    public void setPitchId(String pitchId) {
        this.pitchId = pitchId;
    }*/

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public ArrayList<PitchDateBean> getPitchDateBeenList() {
        return pitchDateBeenList;
    }

    public void setPitchDateBeenList(ArrayList<PitchDateBean> pitchDateBeenList) {
        this.pitchDateBeenList = pitchDateBeenList;
    }

    public boolean isFullPitch() {
        return fullPitch;
    }

    public void setFullPitch(boolean fullPitch) {
        this.fullPitch = fullPitch;
    }
}