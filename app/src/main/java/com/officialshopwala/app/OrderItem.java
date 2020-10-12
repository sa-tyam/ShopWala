package com.officialshopwala.app;

public class OrderItem {
    long orderNumber;
    int price;
    int itemCount;
    String orderTime;
    String orderStatus;
    String address;
    String buyerName;
    int pinCode;
    String buyerMobile;
    String userDescription;

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public OrderItem(long orderNumber, int price, int itemCount, String orderTime, String orderStatus, String address, String buyerName, int pinCode, String buyerMobile) {
        this.orderNumber = orderNumber;
        this.price = price;
        this.itemCount = itemCount;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.address = address;
        this.buyerName = buyerName;
        this.pinCode = pinCode;
        this.buyerMobile = buyerMobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
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
