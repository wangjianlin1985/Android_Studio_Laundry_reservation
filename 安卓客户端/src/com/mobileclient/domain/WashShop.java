package com.mobileclient.domain;

import java.io.Serializable;

public class WashShop implements Serializable {
    /*œ¥“¬µÍ’À∫≈*/
    private String shopUserName;
    public String getShopUserName() {
        return shopUserName;
    }
    public void setShopUserName(String shopUserName) {
        this.shopUserName = shopUserName;
    }

    /*µ«¬º√‹¬Î*/
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*œ¥“¬µÍ√˚≥∆*/
    private String shopName;
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /*œ¥“¬µÍ÷÷¿‡*/
    private int washClassObj;
    public int getWashClassObj() {
        return washClassObj;
    }
    public void setWashClassObj(int washClassObj) {
        this.washClassObj = washClassObj;
    }

    /*œ¥“¬µÍ’’∆¨*/
    private String shopPhoto;
    public String getShopPhoto() {
        return shopPhoto;
    }
    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    /*µÍº“µÁª∞*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*»Î◊§»’∆⁄*/
    private java.sql.Timestamp addDate;
    public java.sql.Timestamp getAddDate() {
        return addDate;
    }
    public void setAddDate(java.sql.Timestamp addDate) {
        this.addDate = addDate;
    }

    /*µÍ∆Ãµÿ÷∑*/
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    /*Œ≥∂»*/
    private float latitude;
    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /*æ≠∂»*/
    private float longitude;
    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

}