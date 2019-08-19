package com.example.storemanager.control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.storemanager.R;
import com.example.storemanager.service.MyFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;

    private List<Fragment> fragments;
    private FragmentPagerAdapter adapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initViwe();
    }

    private void initViwe(){
        mViewPager=findViewById(R.id.fragment_vp);
        mTabRadioGroup=findViewById(R.id.tabs_rg);

        NewsPage newsPage=new NewsPage();

        fragments=new ArrayList<>(4);
        fragments.add(newsPage);

        adapters=new MyFragmentPageAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapters);

        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

    }


    private ViewPager.OnPageChangeListener mPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton=(RadioButton)mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            for (int i=0;i<radioGroup.getChildCount();i++){
                if (radioGroup.getChildAt(i).getId()==checkedId){
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };
}
