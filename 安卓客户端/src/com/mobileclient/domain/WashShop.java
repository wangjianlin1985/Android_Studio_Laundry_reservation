package com.mobileclient.domain;

import java.io.Serializable;

public class WashShop implements Serializable {
    /*ϴ�µ��˺�*/
    private String shopUserName;
    public String getShopUserName() {
        return shopUserName;
    }
    public void setShopUserName(String shopUserName) {
        this.shopUserName = shopUserName;
    }

    /*��¼����*/
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*ϴ�µ�����*/
    private String shopName;
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /*ϴ�µ�����*/
    private int washClassObj;
    public int getWashClassObj() {
        return washClassObj;
    }
    public void setWashClassObj(int washClassObj) {
        this.washClassObj = washClassObj;
    }

    /*ϴ�µ���Ƭ*/
    private String shopPhoto;
    public String getShopPhoto() {
        return shopPhoto;
    }
    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    /*��ҵ绰*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*��פ����*/
    private java.sql.Timestamp addDate;
    public java.sql.Timestamp getAddDate() {
        return addDate;
    }
    public void setAddDate(java.sql.Timestamp addDate) {
        this.addDate = addDate;
    }

    /*���̵�ַ*/
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    /*γ��*/
    private float latitude;
    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /*����*/
    private float longitude;
    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

}