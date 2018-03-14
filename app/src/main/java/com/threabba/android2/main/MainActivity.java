package com.threabba.android2.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.threabba.android.pedometer.R;
import com.threabba.android2.BaseTabActivity;
import com.threabba.android2.flaoting.FloatingService;
import com.threabba.android2.flaoting.FloatingView;

import java.util.ArrayList;

public class MainActivity extends BaseTabActivity {
    /**
     * 1. TBD
     * 1.1. Fragment Restore
     */
    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter();
        mPresenter.onInitialize();
        setupTab();
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setRationaleMessage("we need permission for read contact, find your location and system alert window")
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("setting")
                .setPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SYSTEM_ALERT_WINDOW)
                .check();
        startService(new Intent(this, FloatingService.class));
    }

    @Override
    protected void setupTab() {
        getAdapter()
                .addFragment(MainContract.createFragment(PedometerFragment.class, mPresenter), getString(R.string.tab_pedometer))
                .addFragment(MainContract.createFragment(RecordFragment.class, mPresenter), getString(R.string.tab_record));
        super.setupTab();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }

}
