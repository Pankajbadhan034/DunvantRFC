package com.dunvant.application.beans;

import java.io.Serializable;

public class ConfigueMatchListBean implements Serializable {
    String id;
    String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
