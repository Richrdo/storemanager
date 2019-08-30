package com.example.storemanager.control;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.storemanager.R;
import com.example.storemanager.entity.OrderBean;
import com.example.storemanager.entity.StoreMap;
import com.example.storemanager.service.Checkout;
import com.example.storemanager.service.GetCommodity;
import com.example.storemanager.service.OrderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrderPage  extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private  Activity mActivity;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OrderAdapter adapter;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity= (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        swipeRefreshLayout=view.findViewById(R.id.order_swipe);
        listView=view.findViewById(R.id.order_list);

        swipeRefreshLayout.setOnRefreshListener(this);

        adapter=new OrderAdapter(mActivity,StoreMap.orderBeans);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onRefresh() {
        initOrder();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void initOrder(){
        StoreMap.orderBeans.clear();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("MYTAG", "开始" );
                String url="http://47.106.177.200:8080/store/basket";
                String json=Checkout.streamToString(GetCommodity.doGet(url,"application/json"));
                Log.e("MYTAG", "获取json数据成功,json="+json );
                try{
                    OrderBean orderBean;
                    JSONArray jsonArray=new JSONArray(json);
                    if (jsonArray.length()>0){
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            orderBean=new OrderBean();
                            orderBean.setCommodityID(jsonObject.getInt("commodityID"));
                            orderBean.setUserPhone(jsonObject.getLong("userPhone"));
                            StoreMap.orderBeans.add(orderBean);
                        }
                    }
                }catch (JSONException e){
                    Log.e("MYTAG", "json转换失败，message= "+e.getMessage() );
                }
            }
        });
        thread.start();
        try{
            thread.join();
            Thread.sleep(20);
        }catch (InterruptedException e){
            Log.e("MYTAG", "initOrder: "+e.getMessage() );
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
