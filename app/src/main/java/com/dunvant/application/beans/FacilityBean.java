package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class FacilityBean implements Serializable{

    private String facilityId;
    private String locationName;
    private String locationDescription;
    private String fileTitle;
    private String filePath;
    private String pitchIds;
    private String showPitch;
    private ArrayList<PitchBean> pitchesList = new ArrayList<>();

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
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

    public String getPitchIds() {
        return pitchIds;
    }

    public void setPitchIds(String pitchIds) {
        this.pitchIds = pitchIds;
    }

    public ArrayList<PitchBean> getPitchesList() {
        return pitchesList;
    }

    public void setPitchesList(ArrayList<PitchBean> pitchesList) {
        this.pitchesList = pitchesList;
    }

    public String getShowPitch() {
        return showPitch;
    }

    public void setShowPitch(String showPitch) {
        this.showPitch = showPitch;
    }
}