package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductOnlineShoppingProductDetailsBean implements Serializable {
    String id;
    String name;
    String category;
    String description;
    String image;
    String productType;
    String combination;
    String sku;
    String price;
    String quantity;
    String state;
    String pathImg;
    String files;
    String fileName;
    String galCount;
    ArrayList<String> slider = new ArrayList<>();
    ArrayList<ProductCombBean> productCombBeanArrayList = new ArrayList<>();
    ArrayList<AttributesValuesBean> attributesValuesBeanArrayList = new ArrayList<>();

    public ArrayList<ProductCombBean> getProductCombBeanArrayList() {
        return productCombBeanArrayList;
    }

    public void setProductCombBeanArrayList(ArrayList<ProductCombBean> productCombBeanArrayList) {
        this.productCombBeanArrayList = productCombBeanArrayList;
    }

    public ArrayList<AttributesValuesBean> getAttributesValuesBeanArrayList() {
        return attributesValuesBeanArrayList;
    }

    public void setAttributesValuesBeanArrayList(ArrayList<AttributesValuesBean> attributesValuesBeanArrayList) {
        this.attributesValuesBeanArrayList = attributesValuesBeanArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCombination() {
        return combination;
    }

    public void setCombination(String combination) {
        this.combination = combination;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGalCount() {
        return galCount;
    }

    public void setGalCount(String galCount) {
        this.galCount = galCount;
    }

    public ArrayList<String> getSlider() {
        return slider;
    }

    public void setSlider(ArrayList<String> slider) {
        this.slider = slider;
    }
}
