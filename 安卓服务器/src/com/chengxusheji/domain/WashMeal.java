package com.chengxusheji.domain;

import java.sql.Timestamp;
public class WashMeal {
    /*�ײ�id*/
    private int mealId;
    public int getMealId() {
        return mealId;
    }
    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    /*ϴ���ײ�*/
    private String mealName;
    public String getMealName() {
        return mealName;
    }
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    /*�ײ�˵��*/
    private String introduce;
    public String getIntroduce() {
        return introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    /*�ײͼ۸�*/
    private float price;
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    /*�ײ�ͼƬ*/
    private String mealPhoto;
    public String getMealPhoto() {
        return mealPhoto;
    }
    public void setMealPhoto(String mealPhoto) {
        this.mealPhoto = mealPhoto;
    }

    /*��������*/
    private Timestamp publishDate;
    public Timestamp getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    /*ϴ�µ�*/
    private WashShop washShopObj;
    public WashShop getWashShopObj() {
        return washShopObj;
    }
    public void setWashShopObj(WashShop washShopObj) {
        this.washShopObj = washShopObj;
    }

}