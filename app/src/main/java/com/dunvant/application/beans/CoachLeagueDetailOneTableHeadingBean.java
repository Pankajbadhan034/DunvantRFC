package com.dunvant.application.beans;

import java.io.Serializable;

public class CoachLeagueDetailOneTableHeadingBean implements Serializable {
    String id;
    String label;
    String equation;
    String decimal_places;

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

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public String getDecimal_places() {
        return decimal_places;
    }

    public void setDecimal_places(String decimal_places) {
        this.decimal_places = decimal_places;
    }
}
