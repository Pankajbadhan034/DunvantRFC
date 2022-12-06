package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class AcademySessionBean implements Serializable{

    private String dayName;
    private ArrayList<AcademySessionDetailBean> sessionDetailList = new ArrayList<>();

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public ArrayList<AcademySessionDetailBean> getSessionDetailList() {
        return sessionDetailList;
    }

    public void setSessionDetailList(ArrayList<AcademySessionDetailBean> sessionDetailList) {
        this.sessionDetailList = sessionDetailList;
    }
}