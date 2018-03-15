package com.threabba.android.pedometer.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by jun on 2018-03-15.
 */

public class GPS implements LocationListener, ObservableOnSubscribe<Location> {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private Set<ObservableEmitter<Location>> mEmitters = new HashSet<>();
    private Location mLocation;
    public GPS(Context context){
        if(!startAndLocation(context)){
            throw new RuntimeException("Location Permission denied");
        }
    }

    public boolean startAndLocation(Context context) { //로케이션 등록

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager == null){
            return false;
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    MIN_TIME_BW_UPDATES, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                onLocationChanged(location);
            }
            return true;
        }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    MIN_TIME_BW_UPDATES, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                onLocationChanged(location);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mEmitters.forEach(o -> o.onNext(location));
        }else{
            for(ObservableEmitter<Location> o : mEmitters ){
                o.onNext(location);
            }
        }
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

    @Override
    public void subscribe(ObservableEmitter<Location> emitter) throws Exception {
        mEmitters.add(emitter);
        emitter.setCancellable(() -> {
            emitter.onComplete();
            mEmitters.remove(emitter);
        });
    }
    public static Observable<Location> createObservable(Context context){
        return Observable.create(new GPS(context));
    }
}
