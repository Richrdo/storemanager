package com.example.storemanager.control;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storemanager.MainActivity;
import com.example.storemanager.R;
import com.example.storemanager.service.Checkout;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends AppCompatActivity {
    Button btn_login;
    TextView tv_register;
    EditText account;
    EditText password;

    ResultSet rst;
    Connection conn;
    PreparedStatement pst;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);



        password=(EditText)findViewById(R.id.password);
        account=(EditText)findViewById(R.id.account);
        btn_login=(Button)findViewById(R.id.btn_login);
        tv_register=(TextView)findViewById(R.id.tv_register);



//        跳转到商城主页
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login(){
        String id= account.getText().toString();
        String psw=password.getText().toString();

        if (TextUtils.isEmpty(id)){
            Toast.makeText(LoginPage.this,"账号不能为空",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(psw)){
            Toast.makeText(LoginPage.this,"请输入密码",Toast.LENGTH_SHORT).show();
        }else {
            //调用service的checkout方法来验证身份
            String ori_url = "http://47.106.177.200:8080/store/manager?id=" + id + "&psw=" + psw;
            if (Checkout.isMatch(ori_url)){
                saveCookie(Integer.valueOf(id),psw);
                Toast.makeText(LoginPage.this,"欢迎，管理员 "+id+"号",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginPage.this, HomePage.class);
                startActivity(intent);
            }else{
                Toast.makeText(LoginPage.this,"用户名与密码不符，请重新输入",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void saveCookie(int id,String password){
        SharedPreferences.Editor editor=getSharedPreferences("ManagerData",MODE_PRIVATE).edit();
        editor.putInt("id",id);
        editor.putString("password",password);
        editor.apply();
    }

}
