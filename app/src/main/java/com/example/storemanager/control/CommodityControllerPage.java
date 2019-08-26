package com.example.storemanager.control;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import androidx.fragment.app.Fragment;

import com.example.storemanager.R;
import com.example.storemanager.entity.Commodity;
import com.example.storemanager.entity.StoreMap;
import com.example.storemanager.service.CommodityAdapter;
import com.example.storemanager.service.GetCommodity;
import com.example.storemanager.service.PostChange;


import java.io.File;
import java.util.List;




public class CommodityControllerPage extends Fragment {

    private CommodityAdapter adapter;
    private Activity mActivity;
    private ListView listView;
    private List<Commodity> commodities=null;
    private Button flash=null;
    private SharedPreferences.Editor editor;


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


        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initCommmodity(inflater);
                    }
                }).start();
//                flash.setVisibility(View.GONE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Commodity commodity=commodities.get(i);
                Intent intent=new Intent(mActivity,EditCommodity.class);
                intent.putExtra("click_commodity",commodity);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                AlertDialog alert=builder.setIcon(R.mipmap.ic_launcher_round)
                        .setTitle("提示")
                        .setMessage("确认删除吗？！")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Commodity commodity=commodities.get(position);
                                Log.e("MYTAG", "你将删除"+commodity.getCommodityName());
                                deleteItem(commodity);
                            }
                        }).create();
                alert.show();
                return true;
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
                GetCommodity.getImage(mActivity,commodities);
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

    private void deleteItem(Commodity commodity){
        int id=commodity.getId();
        Thread  deleteFromServe=new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://47.106.177.200:8080/store/change?action=delete&id="+id;
                new PostChange().doPost(url);
                StoreMap.imageMap.remove(id);
                commodities.remove(commodity);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        deleteFromServe.start();
        try{
            deleteFromServe.join();
            Thread.sleep(10);
        }catch (InterruptedException e){
            Log.e("MYTAG", e.getMessage());
        }
    }

}
