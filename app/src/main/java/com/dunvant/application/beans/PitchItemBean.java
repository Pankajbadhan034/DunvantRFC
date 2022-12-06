package com.dunvant.application.beans;

import java.io.Serializable;

public class PitchItemBean implements Serializable{

    String pitchName;
    String pitchAddress;

    public String getPitchName() {
        return pitchName;
    }

    public void setPitchName(String pitchName) {
        this.pitchName = pitchName;
    }

    public String getPitchAddress() {
        return pitchAddress;
    }

    public void setPitchAddress(String pitchAddress) {
        this.pitchAddress = pitchAddress;
    }
}