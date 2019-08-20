package com.example.storemanager.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.storemanager.R;
import com.example.storemanager.service.Checkout;
import com.example.storemanager.service.PostImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsPage extends Fragment {

    private Activity mActivity;
    private ImageView imageView;
    private Button btn_submit;
    private EditText name;
    private EditText price;
    private EditText goods_type;
    private EditText describe;

    private final String requestURL="http://47.106.177.200:8080/store/UploadFileServlet";
    private String picPath=null;
    private static String id;

    private File picFile;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_news,container,false);

        //findView
        imageView=view.findViewById(R.id.goods_image);
        btn_submit=view.findViewById(R.id.submit_commodity);
        name=view.findViewById(R.id.goods_name);
        price=view.findViewById(R.id.goods_price);
        goods_type=view.findViewById(R.id.goods_type);
        describe=view.findViewById(R.id.goods_describe);

        if (mActivity!=null){
            Log.e("MYTAG", "MACTIVITY IS NOT NULL");
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        return view;
    }

    //提交商品信息
    private void submit(){
        if(picFile!=null&&!TextUtils.isEmpty(name.getText())&&!TextUtils.isEmpty(price.getText())&&!TextUtils.isEmpty(goods_type.getText())&&!TextUtils.isEmpty(describe.getText())){
            Log.e("MYTAG", "开始传输文件" );

            String goodsName=name.getText().toString();
            String goodsPrice=price.getText().toString();
            String goodsType=goods_type.getText().toString();
            String goodsDescribe=describe.getText().toString();
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
            String news_date=dateFormat.format(date);

            Thread doPost=new Thread(new Runnable() {
                @Override
                public void run() {
                    id=PostImage.doPost(requestURL,picFile);
                }
            });
            doPost.start();
            try{
                Thread.sleep(500);
                doPost.join();
            }catch (InterruptedException e){
                Log.e("MYTAG", " Thread不能被打断,MESSAGE="+e.getMessage() );
            }
            Thread postCommodity=new Thread(new Runnable() {
                @Override
                public void run() {
                    String ori_url="http://47.106.177.200:8080/store/news?name="+goodsName+"&goodsType="+
                            goodsType+"&id="+id.trim()+"&price="+goodsPrice+"&describe="+goodsDescribe+"&date="+news_date;
                    Log.e("MYTAG", "postURL="+ori_url );
                    Checkout.isMatch(ori_url);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity,"商品上架成功!!",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            postCommodity.start();
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
                Cursor cursor=mActivity.getContentResolver().query(uri,pojo,null,null,null);
                if (cursor!=null){
                    ContentResolver cr=mActivity.getContentResolver();
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
            Toast.makeText(mActivity, "请选择图片", Toast.LENGTH_SHORT).show();
        }else{
            picFile=new File(picPath);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //返回图片到商品详情页
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picFile.toString()));
                }
            });
        }
    }

    //错误提示信息
    private void alert(){
        Dialog dialog=new AlertDialog.Builder(mActivity).setTitle("提示")
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
