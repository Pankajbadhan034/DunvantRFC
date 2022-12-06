package com.dunvant.application.beans;

import java.util.ArrayList;

public class CampSelectedChildBean {

    private ChildBean childBean = new ChildBean();
    private ArrayList<CampDateBean> selectedDatesList = new ArrayList<>();
    private ArrayList<CampWeekBean> selectedWeeksList = new ArrayList<>();

    public ChildBean getChildBean() {
        return childBean;
    }

    public void setChildBean(ChildBean childBean) {
        this.childBean = childBean;
    }

    public ArrayList<CampDateBean> getSelectedDatesList() {
        return selectedDatesList;
    }

    public void setSelectedDatesList(ArrayList<CampDateBean> selectedDatesList) {
        this.selectedDatesList = selectedDatesList;
    }

    public ArrayList<CampWeekBean> getSelectedWeeksList() {
        return selectedWeeksList;
    }

    public void setSelectedWeeksList(ArrayList<CampWeekBean> selectedWeeksList) {
        this.selectedWeeksList = selectedWeeksList;
    }
}