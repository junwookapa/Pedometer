package com.threabba.android.pedometer.components;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.threabba.android2.model.Address;
import com.threabba.android2.http.NaverAPI;
import com.threabba.android2.http.NaverAPIBuilder;
import com.threabba.android.pedometer.App;
import com.threabba.android.util.AsyncCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jun on 16. 12. 4.
 */

public class GPSService extends Service implements LocationListener {

    private final IBinder mGPSBinder = new GPSBinder();
    private CallBack mCallBack;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    public static final int MESSAGE_SUCCESS = 0X1001;
    public static final int MESSAGE_FAIL = 0X1002;
    public static final int MESSAGE_NETWORK_DISCONNECT = 0X1003;
    public static final int MESSAGE_LOCATION_DISABLE = 0X1004;
    public static final int MESSAGE_NO_RESULT_FOUND = 0X1005;
    public static final int MESSAGE_UNKNOWN_ERROR = 0X1006;

    /**
     * 1. 생명 주기 관리
     * **/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mGPSBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    public class GPSBinder extends Binder {
        public GPSService getService() {
            return GPSService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(final Location location) {
        if(mCallBack != null && location != null){
            if(!((App)getApplication()).isNetworkConnected()){
                mCallBack.onUpdateAddress(null, MESSAGE_NETWORK_DISCONNECT);
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("query", location.getLongitude()+","+location.getLatitude());
            NaverAPIBuilder.createAPI(NaverAPI.class)
                    .getAddress(map)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Consumer<Address>() {
                                @Override
                                public void accept(Address o) throws Exception {
                                    if(mCallBack != null){
                                        mCallBack.onUpdateAddress(o.getResult().getItems().get(0).getAddress(), MESSAGE_SUCCESS);
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    mCallBack.onUpdateAddress(null, MESSAGE_NO_RESULT_FOUND);
                                }
                            });
        }
    }

    /**
     * 2. 로케이션 관련
     * **/


    public boolean registerLocation() { //로케이션 등록
        boolean isenable= false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("권한 막힘", "권한막힘");
            return isenable;
        }
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    MIN_TIME_BW_UPDATES, this);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null){
                    onLocationChanged(location);

                }
            }
            isenable = true;
        }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    MIN_TIME_BW_UPDATES, this);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    onLocationChanged(location);
                }
            }
            isenable = true;
        }
        return isenable;
    }

    public Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void registerCallBack(CallBack callBack){
            this.mCallBack = callBack;
            if(registerLocation()){
                Location location = getLastKnownLocation();
                if(location != null){
                    onLocationChanged(location);
                    return;
                }
            }else {
                mCallBack.onUpdateAddress("", MESSAGE_LOCATION_DISABLE);
                return;
            }


    }

    /**
     * 4. 콜백 관련
     * **/
    public void unRegisterCallBack(){
        this.mCallBack = null;
    }

    public interface CallBack{
        void onUpdateAddress(String address,int message);
    }

    private AsyncCallback<String> callback = new AsyncCallback<String>() {
        @Override
        public void onResult(String result) {
            try {
                Address results = new Gson().fromJson(result, Address.class);
                if(mCallBack != null){
                    mCallBack.onUpdateAddress(results.getResult().getItems().get(0).getAddress(), MESSAGE_SUCCESS);
                }
            }catch (NullPointerException e){
                if(mCallBack != null){
                    mCallBack.onUpdateAddress(null, MESSAGE_NO_RESULT_FOUND);
                }
            }
        }

        @Override
        public void exceptionOccured(Exception e) {
            if(mCallBack != null){
                mCallBack.onUpdateAddress(null, MESSAGE_UNKNOWN_ERROR);
            }
        }

        @Override
        public void cancelled() {
        }
    };

}
