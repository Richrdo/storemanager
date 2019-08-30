package com.example.storemanager.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StoreMap {
    public static Map<Integer, Bitmap> imageMap=new HashMap<>();
    public static List<Commodity> commodities=new ArrayList<>();
    public static List<OrderBean> orderBeans=new ArrayList<>();
}
