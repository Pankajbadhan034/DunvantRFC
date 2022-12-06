package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CoachLeagueDetailOneTableDataBean implements Serializable {
//    String p;
//    String w;
//    String d;
//    String l;
//    String pts;
//    String teamName;
//    String logo;

    String label;
    ArrayList<String>values;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
