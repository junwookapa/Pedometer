package com.threabba.android.pedometer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.threabba.android.pedometer.db.Record;
import com.threabba.android.pedometer.db.RecordDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jun on 16. 12. 3.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments;
    public TabPagerAdapter(FragmentManager fm, List<Fragment> fragment) {
        super(fm);
        this.fragments = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("호출 되었당", "호출 : "+position+", "+fragments.size());
        if(position>=0 && position <fragments.size()){
            return fragments.get(position);
        }else{
            return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }
}