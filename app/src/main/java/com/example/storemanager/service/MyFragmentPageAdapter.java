package com.example.storemanager.service;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mlist;

    public MyFragmentPageAdapter(FragmentManager fm,List<Fragment> fragmentList){
        super(fm);
        this.mlist=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mlist==null?null:this.mlist.get(position);
    }

    @Override
    public int getCount() {
        return this.mlist==null?0:this.mlist.size();
    }
}
