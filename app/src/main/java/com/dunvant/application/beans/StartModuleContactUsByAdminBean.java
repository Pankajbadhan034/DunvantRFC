package com.dunvant.application.beans;

import java.io.Serializable;

public class StartModuleContactUsByAdminBean implements Serializable {
    String id;
    String academy_id;
    String label;
    String preferred_text;
    String contact_info;
    String state;
    String created;
    String modified;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(String academy_id) {
        this.academy_id = academy_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPreferred_text() {
        return preferred_text;
    }

    public void setPreferred_text(String preferred_text) {
        this.preferred_text = preferred_text;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
