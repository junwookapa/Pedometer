package com.threabba.android2;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.threabba.android.pedometer.R;
import com.threabba.android.pedometer.db.DaoMaster;
import com.threabba.android.pedometer.db.DaoSession;
import com.threabba.android.pedometer.db.Record;

import io.reactivex.Observable;

/**
 * Created by junwoo on 2016-12-02.
 * https://academy.realm.io/kr/posts/aw210-android-studio-trim-memory/
 */

public class App extends Application{
    //public static final boolean ENCRYPTED = true;
    //private AbstractDaoSession daoSession;

    private DaoSession mDaoSession;
    private Record mRecord;
    private static App app;
    private boolean isForeGround;
    private RXOnChangedAppSchedulingNotifier mRXOnChangedAppScheduling;

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        setScreenOffFilter();
        mRXOnChangedAppScheduling = new RXOnChangedAppSchedulingNotifier();
        app = this;

    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isForeGround = false;
            mRXOnChangedAppScheduling.notify(isForeGround);
        }
    }

    @Override
    public void onTerminate() {
        this.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        super.onTerminate();
    }

    public static SensorManager getSensorManager(){
        SensorManager sensorManager = (SensorManager) app.getSystemService(Context.SENSOR_SERVICE);
        return sensorManager;
    }

    public static Resources getRes(){
        return app.getResources();
    }

    public static DaoSession getDaoSession() {
        if(app.mDaoSession == null){
            DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(app, app.getString(R.string.db_name), null); //create database db file if not exist
            SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
            DaoMaster master = new DaoMaster(db);//create masterDao
            app.mDaoSession = master.newSession(); //Creates Session session
        }
        return app.mDaoSession;
    }

    public static Observable<Boolean> getRXOnChagedAppSchedulingNotifier(){
        return app.mRXOnChangedAppScheduling.getObservable();
    }

    //ref http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null){
            return  netInfo != null && netInfo.isConnectedOrConnecting();
        }else{
            return false;
        }
    }
    public boolean isLocationGPSEnable(){
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return true;
        }else{
            return false;
        }
    }
    public boolean isLocationNetworkEanble(){
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        }else{
            return false;
        }
    }

    private void setScreenOffFilter(){
        IntentFilter screenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!isForeGround) {
                    isForeGround = true;
                    mRXOnChangedAppScheduling.notify(isForeGround);
                }
            }
        }, screenOffFilter);
    }

    ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (!isForeGround) {
                isForeGround = true;
                mRXOnChangedAppScheduling.notify(isForeGround);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };


}
