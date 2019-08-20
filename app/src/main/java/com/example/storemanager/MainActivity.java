package com.example.storemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storemanager.control.HomePage;
import com.example.storemanager.control.LoginPage;
import com.example.storemanager.service.Checkout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    static int id=-1;
    static String psw=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences=getSharedPreferences("ManagerData",MODE_PRIVATE);
        id=preferences.getInt("id",0);
        psw=preferences.getString("password","");

        Log.e("MYTAG", "从内存获取id为"+id+" ,password为"+psw );
        String ori_url = "http://47.106.177.200:8080/store/manager?id=" + id + "&psw=" + psw;
        if (id!=-1&&psw!=null&&Checkout.isMatch(ori_url)){
            Toast.makeText(MainActivity.this,"欢迎，管理员 "+id+"号",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this, LoginPage.class);
            startActivity(intent);
        }
    }
}
