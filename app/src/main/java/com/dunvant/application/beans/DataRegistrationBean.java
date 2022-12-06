package com.dunvant.application.beans;

import java.io.Serializable;

public class DataRegistrationBean implements Serializable {
    private String label_name;
    private String value;

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
