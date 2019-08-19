package com.example.storemanager.service;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class UploadUtil {
    private static final String TAG="MYTAG";
    private static final int TIME_OUT=10*1000;
    private static final String CHARSET="utf-8";

    public static int upLoadFile(File file,String requestURL){
        int res=0;
        String result=null;
        String BOUNDARY= UUID.randomUUID().toString();
        String PREFIX="--",LINE_END="\r\n";
        String CONTENT_TYPE="multipart/form-data";
        try{
            URL url=new URL(requestURL);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset",CHARSET);
            conn.setRequestProperty("Content-Type",CONTENT_TYPE+";boundary="+BOUNDARY);
            if (file!=null){
                Log.e(TAG, "开始上传操作" );
                StringBuffer buffer=new StringBuffer();
                DataOutputStream outputStream=new DataOutputStream(conn.getOutputStream());
                buffer.append(PREFIX);
                buffer.append(BOUNDARY);
                buffer.append(LINE_END);

                buffer.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\""+LINE_END);
                buffer.append("Content-Type:application/octet-stream;charset="+CHARSET+LINE_END);
                buffer.append(LINE_END);
                buffer.append(LINE_END);
                outputStream.write(buffer.toString().getBytes());
                InputStream inputStream=new FileInputStream(file);
                byte[] bytes=new byte[1024];
                int len=0;
                while ((len=inputStream.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                }
                inputStream.close();
                outputStream.write(LINE_END.getBytes());
                byte[] end_data=(PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                outputStream.write(end_data);
                outputStream.flush();

                res=conn.getResponseCode();
                Log.e(TAG, "开始连接，CODE="+res );

                if (res==200){
                    Log.e(TAG, "请求成功" );
                    InputStream inputStream1=conn.getInputStream();
                    StringBuffer sb1=new StringBuffer();
                    int ss ;
                    while ((ss=inputStream.read())!=-1){
                        sb1.append((char)ss);
                    }
                    result=sb1.toString();
                    Log.e(TAG, "result:"+result );
                }else{
                    Log.e(TAG, "请求失败" );
                }
            }
        }catch (MalformedURLException e){
            Log.e(TAG, "尝试连接失败，MESSAGE_1="+e.getMessage() );
        }catch ( IOException e){
            Log.e(TAG, "尝试连接失败，MESSAGE_2="+e.getMessage() );
        }
        return res;
    }
}
