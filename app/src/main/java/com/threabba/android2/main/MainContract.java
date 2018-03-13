package com.threabba.android2.main;

import io.reactivex.Observable;

/**
 * Created by ETRI LSAR Project Team on 2018-03-12.
 */

public class MainContract {

    public interface View{
        void setPresenter(MainContract.Presenter presenter);
    }

    public interface Presenter{
        void onInitialize();
        Observable<Integer> getStepObserver();
    }
}
