package com.dunvant.application.beans;

import java.io.Serializable;

public class CoachPlayerStatsbean implements Serializable {
    String score;
    String attribute_id;
    String timed_value;
    String label;
    String icon_name;
    String icon_type;
    String icon_color;
    String timed_display;

    public String getTimed_display() {
        return timed_display;
    }

    public void setTimed_display(String timed_display) {
        this.timed_display = timed_display;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(String attribute_id) {
        this.attribute_id = attribute_id;
    }

    public String getTimed_value() {
        return timed_value;
    }

    public void setTimed_value(String timed_value) {
        this.timed_value = timed_value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }

    public String getIcon_type() {
        return icon_type;
    }

    public void setIcon_type(String icon_type) {
        this.icon_type = icon_type;
    }

    public String getIcon_color() {
        return icon_color;
    }

    public void setIcon_color(String icon_color) {
        this.icon_color = icon_color;
    }
}
