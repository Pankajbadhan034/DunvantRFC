package com.dunvant.application.beans;

import java.io.Serializable;

public class StartModuleMembershipBean implements Serializable {
    String id;
    String academy_id;
    String title;
    String description;
    String monthly_price;
    String yearly_price;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMonthly_price() {
        return monthly_price;
    }

    public void setMonthly_price(String monthly_price) {
        this.monthly_price = monthly_price;
    }

    public String getYearly_price() {
        return yearly_price;
    }

    public void setYearly_price(String yearly_price) {
        this.yearly_price = yearly_price;
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
