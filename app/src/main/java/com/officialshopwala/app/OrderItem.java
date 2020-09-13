package com.officialshopwala.app;

public class OrderItem {
    long orderNumber;
    int price;
    int itemCount;
    String orderTime;
    String orderStatus;
    public OrderItem() {

    }

    public OrderItem(int itemCount, long orderNumber, String orderStatus, String orderTime, int price) {
        this.itemCount = itemCount;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.price = price;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
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
