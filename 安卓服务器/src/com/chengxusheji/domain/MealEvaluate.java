package com.chengxusheji.domain;

import java.sql.Timestamp;
public class MealEvaluate {
    /*����id*/
    private int evaluateId;
    public int getEvaluateId() {
        return evaluateId;
    }
    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }

    /*�����ײ�*/
    private WashMeal washMealObj;
    public WashMeal getWashMealObj() {
        return washMealObj;
    }
    public void setWashMealObj(WashMeal washMealObj) {
        this.washMealObj = washMealObj;
    }

    /*��������*/
    private String evaluateContent;
    public String getEvaluateContent() {
        return evaluateContent;
    }
    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    /*�����û�*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*����ʱ��*/
    private String evaluateTime;
    public String getEvaluateTime() {
        return evaluateTime;
    }
    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

}