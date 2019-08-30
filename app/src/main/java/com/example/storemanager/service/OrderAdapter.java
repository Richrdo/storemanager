package com.example.storemanager.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.storemanager.R;
import com.example.storemanager.entity.Commodity;
import com.example.storemanager.entity.OrderBean;
import com.example.storemanager.entity.StoreMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class OrderAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<OrderBean> orderBeans;

    public OrderAdapter(Context context, List<OrderBean> orderBeanList){
        orderBeans=orderBeanList;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return orderBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return orderBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View orderItem=inflater.inflate(R.layout.order_item,null);
        OrderBean orderBean=orderBeans.get(i);

        TextView tv_user_phone=orderItem.findViewById(R.id.order_user_phone);
        TextView tv_commodity_name=orderItem.findViewById(R.id.order_commodity_name);

        tv_user_phone.setText("TEL:"+orderBean.getUserPhone());
        Map<Integer, Commodity> commodityMap=StoreMap.commodities.stream().collect(Collectors.toMap(Commodity::getId,a->a,(k1,k2)->k1));
        tv_commodity_name.setText(commodityMap.get(orderBean.getCommodityID()).getCommodityName());


        return orderItem;
    }
}
