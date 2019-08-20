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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.storemanager.R;
import com.example.storemanager.service.PostImage;

import java.io.File;

public class NewsPage extends Fragment {

    private Activity mActivity;
    private ImageView imageView;

    private final String requestURL="http://47.106.177.200:8080/store/UploadFileServlet";
    private String picPath=null;

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
        imageView=view.findViewById(R.id.goods_image);
        if (mActivity!=null){
            Log.e("MYTAG", "MACTIVITY IS NOT NULL");
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
        return view;
    }

    public void selectPhoto(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

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
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picFile.toString()));
                }
            });
        }
    }

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
