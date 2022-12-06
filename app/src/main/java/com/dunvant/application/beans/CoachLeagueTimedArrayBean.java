package com.dunvant.application.beans;

import java.io.Serializable;

public class CoachLeagueTimedArrayBean implements Serializable {

    String id;
    String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
