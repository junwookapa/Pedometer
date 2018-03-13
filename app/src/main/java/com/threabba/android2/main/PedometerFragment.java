package com.threabba.android2.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.threabba.android.pedometer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public class PedometerFragment extends Fragment implements MainContract.View{


    private MainContract.Presenter mPresenter;

    // 복구시 쓰는 상수
    private final String Presenter = "PRESENTER";
    private final String STEP = "STEP";
    private final String ADDRESS = "ADDRESS";
    private final String DIST = "DIST";
    private final String TOGGLE = "TOGGLE";

    /**
     * 1. 위젯 관련
     * **/

    @BindView(R.id.frag_main_tv_step) TextView tv_step;
    @BindView(R.id.frag_main_tv_address) TextView tv_address;
    @BindView(R.id.frag_main_tv_dist) TextView tv_dist;
    @BindView(R.id.frag_main_tgbtn_toggle) ToggleButton tgbtn_toggle;
    @OnClick(R.id.frag_main_tgbtn_toggle)
    void onClick_tgbtn_toggle(){

    }


    /**
     * 1. 생명 주기 관련
     * **/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main, container, false);
        ButterKnife.bind(this, view);
        if(savedInstanceState != null){
            tv_address.setText(savedInstanceState.getString(ADDRESS));
            tv_dist.setText(savedInstanceState.getString(DIST));
            tv_step.setText(savedInstanceState.getString(STEP));
            tgbtn_toggle.setChecked(savedInstanceState.getBoolean(TOGGLE));
        }else{
            initView();
        }
        return view;
    }

    private void initView(){
        tv_address.setText("");
        tv_dist.setText("0KM");
        tv_step.setText("0");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null){
            tv_address.setText(savedInstanceState.getString(ADDRESS));
            tv_dist.setText(savedInstanceState.getString(DIST));
            tv_step.setText(savedInstanceState.getString(STEP));
            tgbtn_toggle.setChecked(savedInstanceState.getBoolean(TOGGLE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STEP, tv_step.getText().toString());
        outState.putString(ADDRESS, tv_address.getText().toString());
        outState.putString(DIST, tv_dist.getText().toString());
        outState.putBoolean(TOGGLE, tgbtn_toggle.isChecked());
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        presenter.getStepObserver().subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("onSubscribe", "onSubscribe : ");
            }

            @Override
            public void onNext(Integer integer) {
                tv_step.setText(integer.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
