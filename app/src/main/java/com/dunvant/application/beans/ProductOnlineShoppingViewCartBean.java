package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductOnlineShoppingViewCartBean implements Serializable {
    String taxLabel;
    String gst;
    String id;
    String cartId;
    String productId;
    String combinationId;
    String availStock;
    String state;
    String createdAt;
    String modifiedAt;
    String productInfoName;
    String productInfoImage;
    String productInfoPrice;
    String productInfoSku;
    String productInfoQuantity;
    String productInfocombination;
    ArrayList<ProductOnlineShoppingAttributeDataBean>productOnlineShoppingAttributeDataBeanArrayList = new ArrayList<>();
    ArrayList<String> combinationArrayList = new ArrayList<>();
    String delCharges;

    public String getDelCharges() {
        return delCharges;
    }

    public void setDelCharges(String delCharges) {
        this.delCharges = delCharges;
    }

    public String getTaxLabel() {
        return taxLabel;
    }

    public void setTaxLabel(String taxLabel) {
        this.taxLabel = taxLabel;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public ArrayList<String> getCombinationArrayList() {
        return combinationArrayList;
    }

    public void setCombinationArrayList(ArrayList<String> combinationArrayList) {
        this.combinationArrayList = combinationArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(String combinationId) {
        this.combinationId = combinationId;
    }

    public String getAvailStock() {
        return availStock;
    }

    public void setAvailStock(String availStock) {
        this.availStock = availStock;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getProductInfoName() {
        return productInfoName;
    }

    public void setProductInfoName(String productInfoName) {
        this.productInfoName = productInfoName;
    }

    public String getProductInfoImage() {
        return productInfoImage;
    }

    public void setProductInfoImage(String productInfoImage) {
        this.productInfoImage = productInfoImage;
    }

    public String getProductInfoPrice() {
        return productInfoPrice;
    }

    public void setProductInfoPrice(String productInfoPrice) {
        this.productInfoPrice = productInfoPrice;
    }

    public String getProductInfoSku() {
        return productInfoSku;
    }

    public void setProductInfoSku(String productInfoSku) {
        this.productInfoSku = productInfoSku;
    }

    public String getProductInfoQuantity() {
        return productInfoQuantity;
    }

    public void setProductInfoQuantity(String productInfoQuantity) {
        this.productInfoQuantity = productInfoQuantity;
    }

    public String getProductInfocombination() {
        return productInfocombination;
    }

    public void setProductInfocombination(String productInfocombination) {
        this.productInfocombination = productInfocombination;
    }

    public ArrayList<ProductOnlineShoppingAttributeDataBean> getProductOnlineShoppingAttributeDataBeanArrayList() {
        return productOnlineShoppingAttributeDataBeanArrayList;
    }

    public void setProductOnlineShoppingAttributeDataBeanArrayList(ArrayList<ProductOnlineShoppingAttributeDataBean> productOnlineShoppingAttributeDataBeanArrayList) {
        this.productOnlineShoppingAttributeDataBeanArrayList = productOnlineShoppingAttributeDataBeanArrayList;
    }
}
