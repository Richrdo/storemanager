package com.example.storemanager.control;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.storemanager.R;
import com.example.storemanager.entity.Commodity;
import com.example.storemanager.entity.StoreMap;
import com.example.storemanager.service.Checkout;
import com.example.storemanager.service.EditKeyListener;
import com.example.storemanager.service.PostImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditCommodity extends AppCompatActivity {

    Commodity commodity;
    private String picPath;
    private EditText goodsType;
    private ImageView goodsImage;
    private EditText goodsName;
    private EditText goodsPrice;
    private EditText goodsDescribe;
    private Button btn_change;
    private Button btn_back;
    private File picFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_commodity);

        goodsDescribe=findViewById(R.id.goods_now_describe);
        goodsImage=findViewById(R.id.goods_now_image);
        goodsName=findViewById(R.id.goods_now_name);
        goodsPrice=findViewById(R.id.goods_now_price);
        goodsType=findViewById(R.id.goods_now_type);
        btn_change=findViewById(R.id.submit_change);
        btn_back=findViewById(R.id.back);
        commodity= (Commodity) getIntent().getSerializableExtra("click_commodity");

        //初始化picFile
        picFile=new File(EditCommodity.this.getFilesDir()+"/goods_image/"+commodity.getId()+".png");

        goodsDescribe.setOnKeyListener(new EditKeyListener());
        goodsImage.setOnKeyListener(new EditKeyListener());
        goodsName.setOnKeyListener(new EditKeyListener());
        goodsPrice.setOnKeyListener(new EditKeyListener());
        goodsType.setOnKeyListener(new EditKeyListener());
        initView();

        //提交修改内容
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_change();
            }
        });
        //返回
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        goodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
    }

    public void initView(){
        goodsPrice.setText(String.valueOf(commodity.getPrice()));
        goodsName.setText(commodity.getCommodityName());
        goodsImage.setImageBitmap(StoreMap.imageMap.get(commodity.getId()));
        goodsDescribe.setText(commodity.getDescribe());
        goodsType.setText(commodity.getType());
    }

    public void back(){
        this.finish();
    }

    public void submit_change() {
        if (!TextUtils.isEmpty(goodsName.getText()) && !TextUtils.isEmpty(goodsPrice.getText()) && !TextUtils.isEmpty(goodsType.getText()) && !TextUtils.isEmpty(goodsDescribe.getText())) {
            Log.e("MYTAG", "开始传输文件");

            String name = goodsName.getText().toString();
            String price = goodsPrice.getText().toString();
            String goods_type = goodsType.getText().toString();
            String goodes_describe = goodsDescribe.getText().toString();

            Thread postCommodity = new Thread(new Runnable() {
                @Override
                public void run() {
                    String ori_url = "http://47.106.177.200:8080/store/change?name=" + name + "&goods_type=" +
                            goods_type + "&price=" + price + "&goods_describe=" + goodes_describe+"&id="+commodity.getId()+"&action=update";
                    Log.e("MYTAG", "更改的url="+ori_url );
                    Checkout.isMatch(ori_url);
                    PostImage.doPost("http://47.106.177.200:8080/store/change_image",picFile);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditCommodity.this, "商品信息修改成功!!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            postCommodity.start();
            try{
                postCommodity.join();
                Thread.sleep(50);
            }catch (InterruptedException e){
                Log.e("MYTAG", "传输文件出错，MESSAGE="+e.getMessage() );
            }
            this.finish();
        }
    }

    //选择图片
    private void selectPhoto(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

    //获取返回信息
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        //当选择的图片不为空时，再获取图片的路径
        if (resultCode== Activity.RESULT_OK){
            Uri uri=data.getData();
            Log.e("MYTAG", "获取图片成功，uri="+uri );
            try{
                String[] pojo={MediaStore.Images.Media.DATA};
                Cursor cursor=getContentResolver().query(uri,pojo,null,null,null);
                if (cursor!=null){
                    ContentResolver cr=getContentResolver();
                    int colunm_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path=cursor.getString(colunm_index);
                    Log.e("MYTAG", "获取的图片path为:"+path );
                    //判断文件的后缀名
                    if (path.endsWith("jpg")||path.endsWith("png")){
                        picPath=path;
                    }else{
                        alert();
                    }
                }else {
                    alert();
                }
            }catch (Exception e){
                Log.e("MYTAG", "好像哪里错了" );
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
        if (picPath==null){
            Toast.makeText(EditCommodity.this, "请选择图片", Toast.LENGTH_SHORT).show();
        }else{
            picFile=new File(picPath);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //返回图片到商品详情页
                    goodsImage.setImageBitmap(BitmapFactory.decodeFile(picFile.toString()));
                }
            });
        }
    }

    //错误提示信息
    private void alert(){
        Dialog dialog=new AlertDialog.Builder(EditCommodity.this).setTitle("提示")
                .setMessage("选择的不是有效图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        picPath=null;
                    }
                }).create();
        dialog.show();
    }
}
