package com.threabba.android.pedometer.components;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.threabba.android.config.Const;
import com.threabba.android.pedometer.App;
import com.threabba.android.pedometer.R;
import com.threabba.android.pedometer.fragments.PedometerFragment;
import com.threabba.android.pedometer.fragments.RecordFragment;
import com.threabba.android.pedometer.fragments.TabPagerAdapter;
import com.threabba.android.pedometer.db.DaoSession;
import com.threabba.android.pedometer.db.Record;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jun on 16. 12. 3.
 */

public class MainActivity extends AppCompatActivity implements PedometerFragment.CallBack {


    // tab layout ref : https://github.com/Swalloow/AndroidStudy
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;

    private DaoSession mDaoSession;
    private App mApp;
    private static final String RECORD = "RECORD";
    //private Record mRecord;
    private static final String ACTIVE = "ACTIVE";
    private boolean mIsActiveStep;
    // 서비스
    private GPSService mGPSService;
    private boolean isGPSServiceBound;
    private StepService mStepService;
    private boolean isStepServiceBound;
    private TabPagerAdapter pagerAdapter;
    private Toast mToast;
    private final DecimalFormat mDecimalFormat = new DecimalFormat("0.##");


    /**
     * 1. 생명주기 관리
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDaoSession = ((App) getApplication()).getDaoSession();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        mApp = (App) getApplication();
        // Initializing the TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("만보기 화면"));
        tabLayout.addTab(tabLayout.newTab().setText("만보기 기록"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, PedometerFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RecordFragment.class.getName()));

        // Creating TabPagerAdapter adapter
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // Set TabSelectedListener

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("we need permission for read contact, find your location and system alert window")
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("setting")
                .setPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SYSTEM_ALERT_WINDOW)
                .check();

/*
        mPedometerFragment = (PedometerFragment) pagerAdapter.getItem(IDX_PEDOMETER_FRAGMENT);
        mRecordFragment = (RecordFragment) pagerAdapter.getItem(IDX_RECORD_FRAGMENT);*/
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        Intent intent = new Intent(this, StepService.class);
        startService(intent);
        bindService(new Intent(MainActivity.this, StepService.class), stepConnection, Context.BIND_AUTO_CREATE);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(!bindGpsService()){
                showGPSAlert();
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            if(!bindGpsService()){
                showGPSAlert();
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.GPS_ARELT:
                bindGpsService();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStepServiceBound && mStepService.isOverrayActive()) {
            mStepService.finishOverayView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                if (isStepServiceBound && !mStepService.isOverrayActive() && mIsActiveStep) {
                    mStepService.initOverayView();
                }
            }
        } else {
            if (isStepServiceBound && !mStepService.isOverrayActive() && mIsActiveStep) {
                mStepService.initOverayView();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        mToast.cancel();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isGPSServiceBound) {
            unbindService(gpsConnection);
        }
        if (isStepServiceBound) {
            unbindService(stepConnection);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mIsActiveStep = savedInstanceState.getBoolean(ACTIVE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ACTIVE, mIsActiveStep);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (savedInstanceState != null) {
            mIsActiveStep = savedInstanceState.getBoolean(ACTIVE);
        }
    }

    /**
     * 2. 서비스 관리
     **/
    private boolean bindGpsService(){
        if (mApp.isLocationNetworkEanble() || mApp.isLocationGPSEnable()) {
            bindService(new Intent(MainActivity.this, GPSService.class), gpsConnection, Context.BIND_AUTO_CREATE);
            return true;
        } else {
            return false;
        }
    }

    ServiceConnection gpsConnection = new ServiceConnection() { //gps 커넥션
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGPSService = ((GPSService.GPSBinder) service).getService();
            if (mGPSService != null) {
                isGPSServiceBound = true;
                mGPSService.registerCallBack(callBack);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isGPSServiceBound = false;
            mGPSService.unRegisterCallBack();
        }
    };

    GPSService.CallBack callBack = new GPSService.CallBack() { // GPS 서비스 콜백
        @Override
        public void onUpdateAddress(String address, int msg) {
            switch (msg) {
                case GPSService.MESSAGE_SUCCESS:
                    ((PedometerFragment) pagerAdapter.instantiateItem(viewPager, 0)).setAddress(address);
                    break;
                case GPSService.MESSAGE_FAIL:
                    break;
                case GPSService.MESSAGE_NETWORK_DISCONNECT:
                    break;
                case GPSService.MESSAGE_LOCATION_DISABLE:
                    break;
                case GPSService.MESSAGE_NO_RESULT_FOUND:
                    break;
                case GPSService.MESSAGE_UNKNOWN_ERROR:
                    break;
            }
        }
    };

    ServiceConnection stepConnection = new ServiceConnection() { //스텝 커넥션
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mStepService = ((StepService.StepServiceBinder) service).getService();
            if (mStepService != null) {
                isStepServiceBound = true;
                //mStepService.registeCallBack(stepListener);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isStepServiceBound = false;
        }
    };

    /*StepListener stepListener = new StepListener() {
        @Override
        public void onStep() { // 스텝 발생 콜백
            if (mIsActiveStep && !mStepService.isOverrayActive()) {
                mApp.onStep();
                Record record = mApp.getRecord();
                ((PedometerFragment) pagerAdapter.instantiateItem(viewPager, 0)).setStep(record.getStep_count() + "");
                ((PedometerFragment) pagerAdapter.instantiateItem(viewPager, 0)).setDist(mDecimalFormat.format(record.getDistance()) + "KM");
                if (record.getStep_count() % 30 == 0) { // 30번마다 한번씩 저장
                    mApp.updateRecord();
                }

                ((RecordFragment) pagerAdapter.instantiateItem(viewPager, 1)).notifyDataSetChanged(mDaoSession);
            } else {

            }
        }

        @Override
        public void passValue() {

        }
    };*/

    @Override
    public void onClick_tgbtn_toggle(boolean isActive) {
        this.mIsActiveStep = isActive;
    }


    /**
     * 4. 기타
     * **/
    private void showGPSAlert() {
        // GPS OFF 일때 Dialog 표시
        AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
        gsDialog.setTitle("위치 서비스 설정");
        gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
        gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // GPS설정 화면으로 이동
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Const.GPS_ARELT);
            }
        }).create().show();

    }

}