package com.mobileclient.domain;

import java.io.Serializable;

public class WashMeal implements Serializable {
    /*套餐id*/
    private int mealId;
    public int getMealId() {
        return mealId;
    }
    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    /*洗衣套餐*/
    private String mealName;
    public String getMealName() {
        return mealName;
    }
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    /*套餐说明*/
    private String introduce;
    public String getIntroduce() {
        return introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    /*套餐价格*/
    private float price;
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    /*套餐图片*/
    private String mealPhoto;
    public String getMealPhoto() {
        return mealPhoto;
    }
    public void setMealPhoto(String mealPhoto) {
        this.mealPhoto = mealPhoto;
    }

    /*发布日期*/
    private java.sql.Timestamp publishDate;
    public java.sql.Timestamp getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(java.sql.Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    /*洗衣店*/
    private String washShopObj;
    public String getWashShopObj() {
        return washShopObj;
    }
    public void setWashShopObj(String washShopObj) {
        this.washShopObj = washShopObj;
    }
	 
    
    /*洗衣店种类*/
    private int washClassObj = 0;
    public int getWashClassObj() {
        return washClassObj;
    }
    public void setWashClassObj(int washClassObj) {
        this.washClassObj = washClassObj;
    }
    
    
    /*排序规则：0代表默认排序,1代表按照距离排序,2代表按价格排序*/
    private int orderRule = 0;
	public void setOrderRule(int orderRule) {
		this.orderRule = orderRule;
	}
	public int getOrderRule() {
		return orderRule;
	}
	
	
	/*用户到洗衣店的距离*/
	private double distance;
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	


}