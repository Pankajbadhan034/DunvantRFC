package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CampBean implements Serializable{

    private String campId;
    private String campName;
    private String fileTitle;
    private String filePath;
    private ArrayList<CampGalleryBean> galleryList = new ArrayList<>();
    private ArrayList<CampLocationBean> locationList = new ArrayList<>();

    private String academiesId;
    private String campDescription;
    private String fromDate;
    private String toDate;
    private String showFromDate;
    private String showToDate;
    private String additionalInformation;
    private String rules;
    private String campIsLocked;
    private String campState;
    private ArrayList<CampDaysBean> daysList = new ArrayList<>();
    private ArrayList<CampSessionBean> sessionsList = new ArrayList<>();

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<CampGalleryBean> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(ArrayList<CampGalleryBean> galleryList) {
        this.galleryList = galleryList;
    }

    public ArrayList<CampLocationBean> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<CampLocationBean> locationList) {
        this.locationList = locationList;
    }

    public String getAcademiesId() {
        return academiesId;
    }

    public void setAcademiesId(String academiesId) {
        this.academiesId = academiesId;
    }

    public String getCampDescription() {
        return campDescription;
    }

    public void setCampDescription(String campDescription) {
        this.campDescription = campDescription;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getCampIsLocked() {
        return campIsLocked;
    }

    public void setCampIsLocked(String campIsLocked) {
        this.campIsLocked = campIsLocked;
    }

    public String getCampState() {
        return campState;
    }

    public void setCampState(String campState) {
        this.campState = campState;
    }

    public ArrayList<CampDaysBean> getDaysList() {
        return daysList;
    }

    public void setDaysList(ArrayList<CampDaysBean> daysList) {
        this.daysList = daysList;
    }

    public ArrayList<CampSessionBean> getSessionsList() {
        return sessionsList;
    }

    public void setSessionsList(ArrayList<CampSessionBean> sessionsList) {
        this.sessionsList = sessionsList;
    }

    public String getShowFromDate() {
        return showFromDate;
    }

    public void setShowFromDate(String showFromDate) {
        this.showFromDate = showFromDate;
    }

    public String getShowToDate() {
        return showToDate;
    }

    public void setShowToDate(String showToDate) {
        this.showToDate = showToDate;
    }
}