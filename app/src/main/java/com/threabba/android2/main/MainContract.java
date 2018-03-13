package com.threabba.android2.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;

public class MainContract {

    public interface View{
        void setPresenter(MainContract.Presenter presenter);
    }

    public interface Presenter{
        void onInitialize();
        Observable<Integer> getStepObserver();
    }

    public static <T extends Fragment & MainContract.View> Fragment createFragment(Class<T> clazz, Presenter presenter){
        try{
            Bundle args = new Bundle();
            T instance = new WeakReference<>(clazz.newInstance()).get();
            instance.setPresenter(presenter);
            instance.setArguments(args);
            return instance;

        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (InstantiationException e){
            e.printStackTrace();
        }
        return null;

    }
}
