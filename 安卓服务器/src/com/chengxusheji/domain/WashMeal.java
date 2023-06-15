package com.chengxusheji.domain;

import java.sql.Timestamp;
public class WashMeal {
    /*Ì×²Íid*/
    private int mealId;
    public int getMealId() {
        return mealId;
    }
    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    /*Ï´ÒÂÌ×²Í*/
    private String mealName;
    public String getMealName() {
        return mealName;
    }
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    /*Ì×²ÍËµÃ÷*/
    private String introduce;
    public String getIntroduce() {
        return introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    /*Ì×²Í¼Û¸ñ*/
    private float price;
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    /*Ì×²ÍÍ¼Æ¬*/
    private String mealPhoto;
    public String getMealPhoto() {
        return mealPhoto;
    }
    public void setMealPhoto(String mealPhoto) {
        this.mealPhoto = mealPhoto;
    }

    /*·¢²¼ÈÕÆÚ*/
    private Timestamp publishDate;
    public Timestamp getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    /*Ï´ÒÂµê*/
    private WashShop washShopObj;
    public WashShop getWashShopObj() {
        return washShopObj;
    }
    public void setWashShopObj(WashShop washShopObj) {
        this.washShopObj = washShopObj;
    }

}