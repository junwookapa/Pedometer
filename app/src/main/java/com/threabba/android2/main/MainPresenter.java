package com.threabba.android2.main;

import android.os.Parcel;
import android.os.Parcelable;

import com.threabba.android2.step.RXStepDetector;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainPresenter implements MainContract.Presenter {
    private Observable<Integer> mStepObserver;

    public MainPresenter(){

    }
    protected MainPresenter(Parcel in) {

    }

    @Override
    public void onInitialize() {
        mStepObserver = RXStepDetector.createObservable().subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Integer> getStepObserver() {
        return mStepObserver;
    }

}
