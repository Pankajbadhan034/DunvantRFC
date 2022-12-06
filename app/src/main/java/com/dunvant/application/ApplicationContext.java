package com.dunvant.application;

import android.app.Application;

import com.dunvant.application.beans.AcademySessionChildBean;
import com.dunvant.application.beans.CoachingAcademyBean;
import com.dunvant.application.beans.PitchBookingSlotsDataBean;

import java.util.ArrayList;

public class ApplicationContext extends Application {

//    HashMap<String, String> bookAcademySummaryData = new HashMap<>();

    CoachingAcademyBean clickedOnAcademy;

    ArrayList<AcademySessionChildBean> academySessionChildBeanListing = new ArrayList<>();
    ArrayList<PitchBookingSlotsDataBean> pitchBookingSlotsDataBeanListing = new ArrayList<>();

    public CoachingAcademyBean getClickedOnAcademy() {
        return clickedOnAcademy;
    }

    public void setClickedOnAcademy(CoachingAcademyBean clickedOnAcademy) {
        this.clickedOnAcademy = clickedOnAcademy;
    }

    public ArrayList<PitchBookingSlotsDataBean> getPitchBookingSlotsDataBeanListing() {
        return pitchBookingSlotsDataBeanListing;
    }

    public void setPitchBookingSlotsDataBeanListing(ArrayList<PitchBookingSlotsDataBean> pitchBookingSlotsDataBeanListing) {
        this.pitchBookingSlotsDataBeanListing = pitchBookingSlotsDataBeanListing;
    }

    public ArrayList<AcademySessionChildBean> getAcademySessionChildBeanListing() {
        return academySessionChildBeanListing;
    }

    public void setAcademySessionChildBeanListing(ArrayList<AcademySessionChildBean> academySessionChildBeanListing) {
        this.academySessionChildBeanListing = academySessionChildBeanListing;
    }

    /*public HashMap<String, String> getBookAcademySummaryData() {
        return bookAcademySummaryData;
    }

    public void setBookAcademySummaryData(HashMap<String, String> bookAcademySummaryData) {
        this.bookAcademySummaryData = bookAcademySummaryData;
    }*/
}