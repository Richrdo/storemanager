package com.example.storemanager.service;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checkout {
    static int stateCode=-1;

    public static boolean isMatch(String ori_url){
        Thread conn=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ori_url);

                    //打开一个HttpURLConnection连接
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    Log.e("MYTAG", "打开Http连接成功");
                    //设置主机连接超时
                    urlConnection.setConnectTimeout(5 * 1000);
                    //设置读取数据超时
                    urlConnection.setReadTimeout(5 * 1000);
                    //设置为GET请求
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setUseCaches(true);
                    urlConnection.addRequestProperty("Connection", "Keep-Alive");
                    //设置请求头信息，获取格式为json
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    Log.e("MYTAG", "url设置成功");

                    //开始连接
                    urlConnection.connect();
                    Log.e("MYTAG", "urlCode=" + urlConnection.getResponseCode());
                    //判断是否连接成功
                    if (urlConnection.getResponseCode() == 200) {
                        Log.e("MYTAG", "urlconnection连接成功！");
                        //获取返回的数据
                        String result = streamToString(urlConnection.getInputStream());
                        //解析JSON
                        Pattern p=Pattern.compile(".*manager.*");
                        Matcher m=p.matcher(ori_url);
                        if (m.matches()){
                            JSONObject jsonObject = new JSONObject(result);
                            stateCode = jsonObject.getInt("code");
                            final String msg = jsonObject.getString("msg");
                            Log.e("MYTAG", "获取json成功，json="+result);
                        }else{
                            Log.e("MYTAG", " 无需获取json" );
                        }
                    }

                } catch (IOException | JSONException e) {
                    Log.e("MalformedURLException", "URL连接失败，MYTAG,MESSAGE=" + e.getMessage());
                    e.printStackTrace();
                }

            }
        });
        conn.start();
        try{
            Thread.sleep(1000);
            conn.join();
        }catch (InterruptedException e){
            Log.e("MYTAG", " 线程异常，MESSAGE="+e.getMessage() );
        }
        Log.e("MYTAG", "stateCode="+stateCode );
        return stateCode==200;
    }

    //将输入流转换为String
    public static String streamToString(InputStream stream){
        byte[] bytes=null;
        try{
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int len=0;
            while((len=stream.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
            outputStream.close();
            stream.close();
            bytes=outputStream.toByteArray();
        }catch (Exception e){
            Log.e("MYTAG", "streaToString失败，MESSAGE="+e.getMessage());

        }
        return new String(bytes);
    }

}
