package com.dunvant.application.beans;

import java.io.Serializable;

public class ChildProfileFieldsDataBean implements Serializable {
    String labelName;
    String value;
    String labelType;
    String slug;
    String buttonName;
    String labelMultiField;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getLabelMultiField() {
        return labelMultiField;
    }

    public void setLabelMultiField(String labelMultiField) {
        this.labelMultiField = labelMultiField;
    }
}
