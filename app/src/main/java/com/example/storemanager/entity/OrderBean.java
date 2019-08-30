package com.example.storemanager.entity;

public class OrderBean {
    private long userPhone;
    private int commodityID;

    public OrderBean() {
    }

    public OrderBean(long userPhone, int commodityID) {
        this.userPhone = userPhone;
        this.commodityID = commodityID;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public int getCommodityID() {
        return commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "userPhone=" + userPhone +
                ", commodityID=" + commodityID +
                '}';
    }
}
