package com.chengxusheji.domain;

import java.sql.Timestamp;
public class MealEvaluate {
    /*评价id*/
    private int evaluateId;
    public int getEvaluateId() {
        return evaluateId;
    }
    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }

    /*被评套餐*/
    private WashMeal washMealObj;
    public WashMeal getWashMealObj() {
        return washMealObj;
    }
    public void setWashMealObj(WashMeal washMealObj) {
        this.washMealObj = washMealObj;
    }

    /*评价内容*/
    private String evaluateContent;
    public String getEvaluateContent() {
        return evaluateContent;
    }
    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    /*评价用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*评价时间*/
    private String evaluateTime;
    public String getEvaluateTime() {
        return evaluateTime;
    }
    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

}