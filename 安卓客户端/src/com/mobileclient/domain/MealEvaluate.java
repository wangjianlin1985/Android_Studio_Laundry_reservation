package com.mobileclient.domain;

import java.io.Serializable;

public class MealEvaluate implements Serializable {
    /*����id*/
    private int evaluateId;
    public int getEvaluateId() {
        return evaluateId;
    }
    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }

    /*�����ײ�*/
    private int washMealObj;
    public int getWashMealObj() {
        return washMealObj;
    }
    public void setWashMealObj(int washMealObj) {
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
    private String userObj;
    public String getUserObj() {
        return userObj;
    }
    public void setUserObj(String userObj) {
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