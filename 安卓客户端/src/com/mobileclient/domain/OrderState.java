package com.mobileclient.domain;

import java.io.Serializable;

public class OrderState implements Serializable {
    /*״̬id*/
    private int orderStateId;
    public int getOrderStateId() {
        return orderStateId;
    }
    public void setOrderStateId(int orderStateId) {
        this.orderStateId = orderStateId;
    }

    /*״̬����*/
    private String stateName;
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

}