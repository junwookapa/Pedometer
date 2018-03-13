package com.threabba.android2.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.threabba.android.pedometer.R;
import com.threabba.android2.BaseTabActivity;
import com.threabba.android2.TabAdapter;
import com.threabba.android2.TabFragmentBuilder;
import com.threabba.android2.step.RXStepDetector;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ETRI LSAR Project Team on 2018-03-13.
 */

public class MainActivity extends BaseTabActivity {

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter();
        mPresenter.onInitialize();
        setupTab();
        mToast = Toast.makeText(this, "스텝업", Toast.LENGTH_SHORT);
    }

    @Override
    protected void setupTab() {
        getAdapter()
                .addFragment(TabFragmentBuilder.build(PedometerFragment.class), "Main")
                .addFragment(TabFragmentBuilder.build(RecordFragment.class), "Record");


        super.setupTab();
    }
    Toast mToast;

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }
}
