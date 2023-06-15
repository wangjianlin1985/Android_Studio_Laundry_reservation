package com.chengxusheji.domain;

import java.sql.Timestamp;
public class OrderInfo {
    /*订单编号*/
    private int orderId;
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /*洗衣套餐*/
    private WashMeal washMealObj;
    public WashMeal getWashMealObj() {
        return washMealObj;
    }
    public void setWashMealObj(WashMeal washMealObj) {
        this.washMealObj = washMealObj;
    }

    /*预订数量*/
    private int orderCount;
    public int getOrderCount() {
        return orderCount;
    }
    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    /*下单用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*联系电话*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*下单时间*/
    private String orderTime;
    public String getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    /*订单状态*/
    private OrderState orderState;
    public OrderState getOrderState() {
        return orderState;
    }
    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    /*备注信息*/
    private String memo;
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

}