package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class AttributesValuesBean implements Serializable {
    String attributeId;
    String attributeName;
    ArrayList<AttributesValuesDataBean>attributesValuesDataBeanArrayList = new ArrayList<>();

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public ArrayList<AttributesValuesDataBean> getAttributesValuesDataBeanArrayList() {
        return attributesValuesDataBeanArrayList;
    }

    public void setAttributesValuesDataBeanArrayList(ArrayList<AttributesValuesDataBean> attributesValuesDataBeanArrayList) {
        this.attributesValuesDataBeanArrayList = attributesValuesDataBeanArrayList;
    }
}
