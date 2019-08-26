package com.example.storemanager.service;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostChange {

    public void doPost(String ori_url){
        try{
            Log.e("MYTAG", "开始修改服务器内容" );
            URL url=new URL(ori_url);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5*1000);
            connection.setRequestMethod("POST");
            connection.connect();
            if (connection.getResponseCode()==200){
                Log.e("MYTAG", "操作成功!" );
            }
        }catch (IOException e){
            Log.e("MYTAG", "POSTCHANGE,操作失败，MESSAGE="+e.getMessage() );
            e.printStackTrace();
        }
    }
}
