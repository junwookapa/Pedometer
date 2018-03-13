package com.threabba.android2.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.threabba.android.pedometer.R;
import com.threabba.android2.BaseTabActivity;

public class MainActivity extends BaseTabActivity {
    /**
     * 1. TBD
     * 1.1. Fragment Restore
     */
    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter();
        mPresenter.onInitialize();
        setupTab();
    }

    @Override
    protected void setupTab() {
        getAdapter()
                .addFragment(MainContract.createFragment(PedometerFragment.class, mPresenter), getString(R.string.tab_pedometer))
                .addFragment(MainContract.createFragment(RecordFragment.class, mPresenter), getString(R.string.tab_record));
        super.setupTab();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }
}
