package com.example.storemanager.service;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.storemanager.R;
import com.example.storemanager.entity.Commodity;
import com.example.storemanager.entity.StoreMap;

import org.w3c.dom.Text;

import java.util.List;
/*
*自定义适配器；
 */
public class CommodityAdapter extends BaseAdapter {
    private List<Commodity> commodities;
    private LayoutInflater inflater;

    public CommodityAdapter(Context context, List<Commodity> commodities){
        this.commodities=commodities;
        this.inflater=LayoutInflater.from(context);
    }

    //getCount获取总item数量
    @Override
    public int getCount() {
        return commodities==null?0:commodities.size();
    }

    //根据位置获取item对象
    @Override
    public Object getItem(int i) {
        return commodities.get(i);
    }

    //根据位置获取item的id
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.e("mytag", "adapter.getView开始执行" );
        View viewCommodity=inflater.inflate(R.layout.commodity_list,null);
        Commodity commodity=commodities.get(i);

        TextView commodity_name=viewCommodity.findViewById(R.id.commodity_name);
        TextView commodity_price=viewCommodity.findViewById(R.id.commodity_price);
        TextView commodity_describe=viewCommodity.findViewById(R.id.commodity_describe);
        ImageView commodity_iamge=viewCommodity.findViewById(R.id.commodity_image);

        if (commodity.getDescribe()!=null&&commodity.getId()>=0&&commodity.getCommodityName()!=null&&StoreMap.imageMap.get(commodity.getId())!=null){
            Log.e("MYTAG", " 开始加载，commodity="+commodity.toString() );
            commodity_name.setText(commodity.getCommodityName());
            commodity_describe.setText(commodity.getDescribe());
            commodity_price.setText(String.valueOf(commodity.getPrice()));
            commodity_iamge.setImageBitmap(StoreMap.imageMap.get(commodity.getId()));
        }else{
            Log.e("MYTAG", "空指针异常" );
        }
        return viewCommodity;
    }
}
