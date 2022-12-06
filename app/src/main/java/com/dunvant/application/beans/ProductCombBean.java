package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductCombBean implements Serializable {
    ArrayList<String> combIdArray = new ArrayList<>();
    String sku;
    String price;
    String quantity;


    public ArrayList<String> getCombIdArray() {
        return combIdArray;
    }

    public void setCombIdArray(ArrayList<String> combIdArray) {
        this.combIdArray = combIdArray;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
