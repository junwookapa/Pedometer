package com.threabba.android2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by ETRI LSAR Project Team on 2018-03-13.
 */

public class TabFragmentBuilder{
    public static Fragment build(Class<? extends Fragment> clazz){
        try{
            Bundle args = new Bundle();

            Fragment fragment = clazz.newInstance();
            fragment.setArguments(args);
            return fragment;

        }catch (IllegalAccessException e){
            Log.e("에라", "에라 :"+e.getMessage());
        }catch (InstantiationException e){
            Log.e("에라", "에라 :"+e.getMessage());
        }
        return null;

    }
}
