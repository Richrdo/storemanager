package com.example.storemanager.control;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.storemanager.R;
import com.example.storemanager.entity.Commodity;
import com.example.storemanager.service.CommodityAdapter;
import com.example.storemanager.service.GetCommodity;

import java.util.List;
import java.util.Map;


public class CommodityControllerPage extends Fragment {

    CommodityAdapter adapter;
    private Activity mActivity;
    ListView listView;
    List<Commodity> commodities=null;
    Button flash=null;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mActivity=(Activity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commodity_controller, null, false);
        listView=view.findViewById(R.id.commodity_list_view);
        flash=view.findViewById(R.id.flash_goods);

//        initLocalCommodity();

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initCommmodity(inflater);
                    }
                }).start();
                flash.setVisibility(View.GONE);
            }
        });

        return view;
    }

    public void initCommmodity(LayoutInflater inflater){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("MYTAG", "开启getCommodity" );
               commodities=GetCommodity.initCommodity("http://47.106.177.200:8080/store/get_commodity");
                GetCommodity.downloadImage(commodities,mActivity);
            }
        });
        thread.start();
        try{
            Log.e("mytag", "前奏" );
            thread.join();
            Thread.sleep(1000);
        }catch (InterruptedException e){
            Log.e("MYTAG", "线程问题，MESSAGE="+e.getMessage() );
        }
        Log.e("MYTAG", "开始设置适配器" );
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter=new CommodityAdapter(mActivity,commodities);
                listView.setAdapter(adapter);
            }
        });

    }

}
