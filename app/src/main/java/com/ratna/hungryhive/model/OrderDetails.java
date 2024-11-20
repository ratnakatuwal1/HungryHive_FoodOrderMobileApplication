package com.ratna.hungryhive.model;

import java.util.List;

public class OrderDetails {
    String userUid = null;
    String userName;
    String email;
    List<String> foodNames;
    List<String> foodImages;
    List<String> foodPrices;
    List<String> foodQuantities;
    String address;
    String totalPrices;
    String phoneNumber;
    Boolean orderAccepted = false;
    Boolean paymentReceived = false;
    String itemPushKey;
    Long currentTime;

    public OrderDetails() {

    }

    public OrderDetails(String userUid, String userName, String email, List<String> foodNames, List<String> foodImages, List<String> foodPrices, List<String> foodQuantities, String address, String totalPrices, String phoneNumber, Boolean orderAccepted, Boolean paymentReceived, String itemPushKey, Long currentTime) {
        this.userUid = userUid;
        this.userName = userName;
        this.email = email;
        this.foodNames = foodNames;
        this.foodImages = foodImages;
        this.foodPrices = foodPrices;
        this.foodQuantities = foodQuantities;
        this.address = address;
        this.totalPrices = totalPrices;
        this.phoneNumber = phoneNumber;
        this.orderAccepted = orderAccepted;
        this.paymentReceived = paymentReceived;
        this.itemPushKey = itemPushKey;
        this.currentTime = currentTime;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getFoodNames() {
        return foodNames;
    }

    public void setFoodNames(List<String> foodNames) {
        this.foodNames = foodNames;
    }
    public List<String> getFoodImages() {
        return foodImages;
    }
    public void setFoodImages(List<String> foodImages) {
        this.foodImages = foodImages;
    }

    public List<String> getFoodPrices() {
        return foodPrices;
    }

    public void setFoodPrices(List<String> foodPrices) {
        this.foodPrices = foodPrices;
    }

    public List<String> getFoodQuantities() {
        return foodQuantities;
    }

    public void setFoodQuantities(List<String> foodQuantities) {
        this.foodQuantities = foodQuantities;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalPrices() {
        return totalPrices;
    }

    public void setTotalPrices(String totalPrices) {
        this.totalPrices = totalPrices;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getOrderAccepted() {
        return orderAccepted;
    }

    public void setOrderAccepted(Boolean orderAccepted) {
        this.orderAccepted = orderAccepted;
    }

    public Boolean getPaymentReceived() {
        return paymentReceived;
    }

    public void setPaymentReceived(Boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public String getItemPushKey() {
        return itemPushKey;
    }

    public void setItemPushKey(String itemPushKey) {
        this.itemPushKey = itemPushKey;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }
}
