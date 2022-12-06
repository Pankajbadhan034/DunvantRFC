package com.dunvant.application.beans;

import java.io.Serializable;

public class GetRegFormBean implements Serializable {
    String id;
    String labelName;
    String slug;
    String isShow;
    String isRequired;
    String fieldType;
    String inputTypeMultipleField;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getInputTypeMultipleField() {
        return inputTypeMultipleField;
    }

    public void setInputTypeMultipleField(String inputTypeMultipleField) {
        this.inputTypeMultipleField = inputTypeMultipleField;
    }
}
