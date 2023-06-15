package com.mobileclient.domain;

import java.io.Serializable;

public class WashMeal implements Serializable {
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
    private java.sql.Timestamp publishDate;
    public java.sql.Timestamp getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(java.sql.Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    /*ϴ�µ�*/
    private String washShopObj;
    public String getWashShopObj() {
        return washShopObj;
    }
    public void setWashShopObj(String washShopObj) {
        this.washShopObj = washShopObj;
    }
	 
    
    /*ϴ�µ�����*/
    private int washClassObj = 0;
    public int getWashClassObj() {
        return washClassObj;
    }
    public void setWashClassObj(int washClassObj) {
        this.washClassObj = washClassObj;
    }
    
    
    /*�������0����Ĭ������,1�����վ�������,2�����۸�����*/
    private int orderRule = 0;
	public void setOrderRule(int orderRule) {
		this.orderRule = orderRule;
	}
	public int getOrderRule() {
		return orderRule;
	}
	
	
	/*�û���ϴ�µ�ľ���*/
	private double distance;
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	


}