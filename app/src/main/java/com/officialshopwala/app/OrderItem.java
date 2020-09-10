package com.officialshopwala.app;

public class OrderItem {
    String orderNumber;
    String price;
    String itemCount;
    String orderTime;
    String orderStatus;
    public OrderItem() {

    }

    public OrderItem(String itemCount, String orderNumber, String orderStatus, String orderTime, String price) {
        this.itemCount = itemCount;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.price = price;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
