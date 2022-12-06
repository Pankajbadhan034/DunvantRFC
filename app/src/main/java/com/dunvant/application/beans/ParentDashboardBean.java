package com.dunvant.application.beans;

import java.io.Serializable;

public class ParentDashboardBean implements Serializable {
    String id;
    String navigation_type;
    String label;
    String academy_id;
    String status;
    String preferred_text;
    String sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNavigation_type() {
        return navigation_type;
    }

    public void setNavigation_type(String navigation_type) {
        this.navigation_type = navigation_type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(String academy_id) {
        this.academy_id = academy_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreferred_text() {
        return preferred_text;
    }

    public void setPreferred_text(String preferred_text) {
        this.preferred_text = preferred_text;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
