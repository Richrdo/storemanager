package com.example.storemanager.service;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Scanner;

public class PostImage {

    public static String doPost(String urlString, File picFile) {
        StringBuilder response=null;
        try{
            Log.e("MYTAG", "doPost开始");
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            Log.e("MYTAG", "connection成功");
            //try里面拿到输出流，输出端就是服务器端
            try (BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream())) {

                InputStream is = new FileInputStream(picFile);
                Log.e("MYTAG", "fileinputstream成功");
                BufferedInputStream bis = new BufferedInputStream(is);
                Log.e("MYTAG", "bufferedinpustream成功");
                byte[] buf= new byte[1024];
                int length = 0;
                length = bis.read(buf);
                while(length!=-1) {
                    bos.write(buf, 0, length);
                    length = bis.read(buf);
                }
                Log.e("MYTAG", "while成功");
                bis.close();
                is.close();
                bos.close();
            }

            //下面是服务器端如果有返回数据的话，做接收用的
            response = new StringBuilder();
            try (Scanner in = new Scanner(connection.getInputStream())) {
                while (in.hasNextLine()) {
                    response.append(in.nextLine());
                    response.append("\n");
                    Log.e("MYTAG", "返回数据获取成功");
                }
            } catch (IOException e) {
                if (!(connection instanceof HttpURLConnection))
                    throw e;
                InputStream err = ((HttpURLConnection) connection).getErrorStream();
                if (err == null)
                    throw e;
                Log.e("MYTAG", "返回数据获取失败");
                Scanner in = new Scanner(err);
                response.append(in.nextLine());
                response.append("\n");
                in.close();
            }
        }catch (IOException  e){
            Log.e("MYTAG", "url出错，message= "+e.getMessage() );
        }


        Log.e("MYTAG", "result="+response.toString() );
        return response.toString();
    }

}
