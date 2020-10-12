package com.officialshopwala.app;

public class ProductItem {
    long productId;
    String user;
    int price;
    String name;
    String productCategory;
    String quantityType;
    String description;
    String productImageUrl;

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public ProductItem(long productId, String user, int price, String name, String productCategory, String quantityType, String description, String productImageUrl) {
        this.productId = productId;
        this.user = user;
        this.price = price;
        this.name = name;
        this.productCategory = productCategory;
        this.quantityType = quantityType;
        this.description = description;
        this.productImageUrl = productImageUrl;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
