package com.threabba.android2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.threabba.android.pedometer.R;
import com.threabba.android2.main.MainPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ETRI LSAR Project Team on 2018-03-13.
 */

public abstract class BaseTabActivity extends AppCompatActivity{

    private TabAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void setupTab(){
        ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(getAdapter());
        tabLayout.setupWithViewPager(viewPager);
    }

    protected TabAdapter getAdapter(){
        if(null ==  mAdapter){
            mAdapter = new TabAdapter(getSupportFragmentManager());
        }
        return mAdapter;
    }

}
