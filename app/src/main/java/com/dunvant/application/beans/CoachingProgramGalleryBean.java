package com.dunvant.application.beans;

import java.io.Serializable;

public class CoachingProgramGalleryBean implements Serializable{

    private String fileTitle;
    private String fileType;
    private String filePath;

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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}