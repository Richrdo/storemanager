package com.example.storemanager.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commodity implements Serializable {
    private String imagePath;
    private String commodityName;
    private String describe;
    private double price;
    private String type;
    private String groundDate;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Commodity() {
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
        groundDate=dateFormat.format(date);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroundDate() {
        return groundDate;
    }
}
