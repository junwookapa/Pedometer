package com.threabba.android.pedometer;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.threabba.android.config.Const;
import com.threabba.android.pedometer.db.DaoMaster;
import com.threabba.android.pedometer.db.DaoSession;
import com.threabba.android.pedometer.db.Record;

/**
 * Created by junwoo on 2016-12-02.
 */

public class App extends Application{
    //public static final boolean ENCRYPTED = true;
    //private AbstractDaoSession daoSession;
    private final String DB_NAME ="pedometer" ;
    private DaoSession mDaoSession;



    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
    }

    private void initDB(){
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this, Const.DB_NAME, null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        DaoMaster master = new DaoMaster(db);//create masterDao
        this.mDaoSession =master.newSession(); //Creates Session session
    }

    public DaoSession getDaoSession() {
        if(mDaoSession != null){
            return mDaoSession;
        }else{
            initDB();
            return mDaoSession;
        }
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

}
