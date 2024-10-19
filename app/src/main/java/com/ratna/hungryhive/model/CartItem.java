package com.ratna.hungryhive.model;

public class CartItem {
    private String foodName;
    private String foodPrice;
    private String foodDescription;
    private String foodImage;
    private String foodIngredients;
    private Integer foodQuantity;

    public CartItem(){

    }

    public CartItem(String foodName, String foodPrice, String foodDescription, String foodImage, String foodIngredients, int foodQuantity) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.foodImage = foodImage;
        this.foodIngredients = foodIngredients;
        this.foodQuantity = foodQuantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodIngredients() {
        return foodIngredients;
    }

    public void setFoodIngredients(String foodIngredients) {
        this.foodIngredients = foodIngredients;
    }

    public Integer getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(Integer foodQuantity) {
        this.foodQuantity = foodQuantity;
    }
}
