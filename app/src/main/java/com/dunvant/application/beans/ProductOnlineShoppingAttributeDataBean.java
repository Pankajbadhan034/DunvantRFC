package com.dunvant.application.beans;

import java.io.Serializable;

public class ProductOnlineShoppingAttributeDataBean implements Serializable {
    String name;
    String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
