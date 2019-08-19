package com.example.storemanager.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.storemanager.R;

public class NewsPage extends Fragment {

    Activity mActivity;
    ImageView imageView;

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
                Intent intent=new Intent(mActivity,ChoosePhoto.class);
                mActivity.startActivity(intent);
            }
        });

        return view;
    }



}
