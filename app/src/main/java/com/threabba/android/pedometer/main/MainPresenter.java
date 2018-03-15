package com.threabba.android.pedometer.main;

import android.content.Context;
import android.util.Log;

import com.threabba.android.pedometer.http.NaverAPI;
import com.threabba.android.pedometer.http.NaverAPIBuilder;
import com.threabba.android.pedometer.location.GPS;
import com.threabba.android2.model.Address;
import com.threabba.android.pedometer.step.RXStepDetector;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {
    private Observable<Integer> mStepObservable;
    private Observable<Address> mAddressObservable;
    private Context mContext;
    public MainPresenter(Context context){
        mContext = context;
    }


    @Override
    public void onInitialize() {
        mStepObservable = RXStepDetector.createObservable();
        mAddressObservable = GPS.createObservable(mContext).flatMap(location -> {
            Map<String, String> map = new HashMap<>();
            Log.e("헬로우월드", "헬로우월드 :"+location.getLongitude()+","+location.getLatitude());
            map.put("query", location.getLongitude()+","+location.getLatitude());
            return NaverAPIBuilder.createAPI(NaverAPI.class).getAddress(map).subscribeOn(Schedulers.io());
        });
    }

    @Override
    public Observable<Integer> getStepObserver() {
        return mStepObservable;
    }

    @Override
    public Observable<Address> getAddressObserver() {
        return mAddressObservable;
    }


}
