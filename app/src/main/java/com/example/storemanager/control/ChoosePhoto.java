package com.example.storemanager.control;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.storemanager.R;
import com.example.storemanager.service.UploadUtil;

import java.io.File;

public class ChoosePhoto extends AppCompatActivity {

    private static String requestURL="http://localhost:9999/Struts_Study/UploadFileServlet";
    private ImageView imageView;
    String picPath=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);

        Log.e("MYTAG", "CHOOSEPHOTO CHEATE" );
        init();
    }

    public void init(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
        if (picPath==null){
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
        }else{
            final File file=new File(picPath);
            if (file!=null){
                int request= UploadUtil.upLoadFile(file,requestURL);
            }
        }
    }

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
                    ContentResolver cr=this.getContentResolver();
                    int colunm_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path=cursor.getString(colunm_index);
                    Log.e("MYTAG", "获取的图片path为:"+path );
                    //判断文件的后缀名
                    if (path.endsWith("jpg")||path.endsWith("png")){
                        picPath=path;
                        Bitmap bitmap= BitmapFactory.decodeStream(cr.openInputStream(uri));
                        imageView.setImageBitmap(bitmap);
                    }else{
                        alert();
                    }
                }else {
                    alert();
                }
            }catch (Exception e){

            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void alert(){
        Dialog dialog=new AlertDialog.Builder(this).setTitle("提示")
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
