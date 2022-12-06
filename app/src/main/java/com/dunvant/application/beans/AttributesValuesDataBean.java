package com.dunvant.application.beans;

import java.io.Serializable;

public class AttributesValuesDataBean implements Serializable {
    String attribute_value_id;
    String attribute_value_name;
    String clickedBool;
    String attribute_name;

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public String getClickedBool() {
        return clickedBool;
    }

    public void setClickedBool(String clickedBool) {
        this.clickedBool = clickedBool;
    }


    public String getAttribute_value_id() {
        return attribute_value_id;
    }

    public void setAttribute_value_id(String attribute_value_id) {
        this.attribute_value_id = attribute_value_id;
    }

    public String getAttribute_value_name() {
        return attribute_value_name;
    }

    public void setAttribute_value_name(String attribute_value_name) {
        this.attribute_value_name = attribute_value_name;
    }
}
