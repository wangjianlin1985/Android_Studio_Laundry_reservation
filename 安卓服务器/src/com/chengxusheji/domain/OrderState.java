package com.chengxusheji.domain;

import java.sql.Timestamp;
public class OrderState {
    /*×´Ì¬id*/
    private int orderStateId;
    public int getOrderStateId() {
        return orderStateId;
    }
    public void setOrderStateId(int orderStateId) {
        this.orderStateId = orderStateId;
    }

    /*×´Ì¬Ãû³Æ*/
    private String stateName;
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

}