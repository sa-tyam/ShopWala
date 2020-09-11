package com.officialshopwala.app;

public class Category {
    String categoryName;
    int numberOfProducts;

    public Category (String categoryName, int numberOfProducts) {
        this.categoryName = categoryName;
        this.numberOfProducts = numberOfProducts;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }
}
