package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CoachingAcademyBean implements Serializable{

    private String academyId;
    private String coachingProgramName;
    private String description;
    private String isTrial;
    private String fileTitle;
    private String filePath;
    private ArrayList<CoachingProgramGalleryBean> galleryList = new ArrayList<>();

    public String getAcademyId() {
        return academyId;
    }

    public void setAcademyId(String academyId) {
        this.academyId = academyId;
    }

    public String getCoachingProgramName() {
        return coachingProgramName;
    }

    public void setCoachingProgramName(String coachingProgramName) {
        this.coachingProgramName = coachingProgramName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
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

    public ArrayList<CoachingProgramGalleryBean> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(ArrayList<CoachingProgramGalleryBean> galleryList) {
        this.galleryList = galleryList;
    }
}