package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class PitchSlotsBean implements Serializable {
    String pitchId;
    String pitchName;
    String pitchPrice;
    ArrayList<PitchSlotsRowBean>stringArrayList = new ArrayList<>();

    public String getPitchId() {
        return pitchId;
    }

    public void setPitchId(String pitchId) {
        this.pitchId = pitchId;
    }

    public String getPitchName() {
        return pitchName;
    }

    public void setPitchName(String pitchName) {
        this.pitchName = pitchName;
    }

    public String getPitchPrice() {
        return pitchPrice;
    }

    public void setPitchPrice(String pitchPrice) {
        this.pitchPrice = pitchPrice;
    }

    public ArrayList<PitchSlotsRowBean> getStringArrayList() {
        return stringArrayList;
    }

    public void setStringArrayList(ArrayList<PitchSlotsRowBean> stringArrayList) {
        this.stringArrayList = stringArrayList;
    }
}
