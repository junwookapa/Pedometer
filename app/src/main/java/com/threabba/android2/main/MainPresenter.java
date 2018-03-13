package com.threabba.android2.main;

import com.threabba.android2.step.RXStepDetector;

import io.reactivex.Observable;

/**
 * Created by ETRI LSAR Project Team on 2018-03-13.
 */

public class MainPresenter implements MainContract.Presenter{
    private Observable<Integer> mStepObserver;
    @Override
    public void onInitialize() {
        mStepObserver = RXStepDetector.createObservable();
    }

    @Override
    public Observable<Integer> getStepObserver() {
        return mStepObserver;
    }
}
