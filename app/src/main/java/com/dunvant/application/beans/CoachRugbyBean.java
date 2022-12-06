package com.dunvant.application.beans;

import java.io.Serializable;

public class CoachRugbyBean implements Serializable {
    String id;
    String title;
    String fileName;
    String documentFor;
    String state;
    String ext;
    String fileUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocumentFor() {
        return documentFor;
    }

    public void setDocumentFor(String documentFor) {
        this.documentFor = documentFor;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
